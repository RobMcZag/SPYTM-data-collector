package com.robertozagni.SPYTM.data.datalake.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import net.snowflake.client.jdbc.SnowflakeConnection;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;

import static com.robertozagni.SPYTM.data.datalake.service.SnowflakeStorageService.SnowflakeStorageConfig.*;

@Slf4j
@Service
public class SnowflakeStorageService {

    private final SnowflakeBasicDataSource sfDatasource;
    private final CsvService csvService;
    private PreparedStatement mergeIntoMetadataStatement;

    @Autowired
    public SnowflakeStorageService(@Qualifier("SnowflakeBasicDataSource") SnowflakeBasicDataSource sfDatasource,
                                   CsvService csvService) throws SQLException {
        this.sfDatasource = sfDatasource;
        this.csvService = csvService;

        try {
            checkConnectionToSF();

            applyFlywayMigrationsToSF();

            mergeIntoMetadataStatement = sfDatasource.getConnection()
                    .prepareStatement(String.format(MERGE_METADATA_SQL, METADATA_TABLE_NAME));

        } catch (SQLException e) {
            log.error("Cannot apply migrations or prepare statements on Snowflake."
                    + " - Error: " + e.getMessage() +" - SQL State: "+ e.getSQLState());
            throw e;
        }

    }

    private void applyFlywayMigrationsToSF() {
        Flyway flyway = Flyway.configure()
                .dataSource(sfDatasource)
                .locations("db/snowflake/migration")
                .load();

        flyway.migrate();
    }
    public void checkConnectionToSF() throws SQLException {
        sfDatasource.getConnection().createStatement().execute("Select 1;");    // Just check we can run a query
        log.info("Connection to Snowflake is working. :) ");
    }

    /**
     * Utility method to load both Metadata and Quotes in one step to Snowflake.
     * @param timeSerie The time serie to be loaded in snowflake.
     */
    public void load(TimeSerie timeSerie) {
        loadMetadata(timeSerie.getMetadata());
        loadQuotes(timeSerie);
    }

    public void loadMetadata(TimeSerieMetadata metadata) {
        try {
            loadMetadataToSF(metadata);
        } catch (SQLException e) {
            log.error("Could not load Metadata to Snowflake for " + metadata.getId()
                    + " - Error: " + e.getMessage() +" - SQL State: "+ e.getSQLState());
        }
    }

    private void loadMetadataToSF(TimeSerieMetadata metadata) throws SQLException {
        mergeIntoMetadataStatement.setString(1, metadata.getProvider().toString());
        mergeIntoMetadataStatement.setString(2, metadata.getQuotetype().toString());
        mergeIntoMetadataStatement.setString(3, metadata.getSymbol());
        mergeIntoMetadataStatement.setString(4, metadata.getDescription());
        mergeIntoMetadataStatement.setString(5, metadata.getTimeZone());
        mergeIntoMetadataStatement.setString(6, metadata.getLastRefreshed());
        mergeIntoMetadataStatement.execute();
    }

    /**
     * Loads the given quotes to Snowflake, using the configured values from this service configuration.
     * @param timeSerie The time serie to be loaded.
     */
    public void loadQuotes(TimeSerie timeSerie) {
        String csvQuotes = quotesToCSV(timeSerie);
        if (csvQuotes != null) {
            loadCsvQuotesToSF(
                    new ByteArrayInputStream(csvQuotes.getBytes(StandardCharsets.UTF_8)),
                    timeSerie.getMetadata().getSymbol(),
                    timeSerie.getMetadata().getQuotetype());
        }
    }

    private String quotesToCSV(TimeSerie timeSerie) {
        try {
            return csvService.quotesToCSV(timeSerie);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error("Counld not convert TimeSerie to CSV. " + timeSerie.getMetadata(), e);
        } catch (IOException e) {
            log.error("Could not write CSV to IO. Timeserie contains " +timeSerie.getData().size() + " quotes. "
                            +timeSerie.getMetadata(), e);
        }
        return null;
    }

    /**
     * Loads the quotes represented as CSV in the inputStream to Snowflake.
     * @param inputStream A stream from which to read the quotes to be loaded, in CSV format.
     * @param quoteType The type of quote being loaded.
     * @param symbol The symbol of the security being loaded.
     */
    private void loadCsvQuotesToSF(InputStream inputStream, String symbol, QuoteType quoteType) {
        String destFileName = getNewCsvFileName();     // Produces a different name every call... :)
        try {
            loadDataToStage(inputStream, symbol, quoteType, destFileName);

            copyFromStageToTable(
                    getStagePathToFile(quoteType, symbol, destFileName),
                    getTableName(quoteType));

            // TODO handle deduplication at load time
            // TODO drop file after successful load

        } catch (SQLException e) {
            log.error(String.format(
                    "ERROR loading data from stage '%s' to table '%s' for Symbol %s (Quote type: %s)",
                    getStageName(), getTableName(quoteType), symbol, quoteType) );
        }
    }



    private void loadDataToStage(InputStream inputStream, String symbol, QuoteType quoteType, String destFileName) throws SQLException {
        try {
            sfDatasource.getConnection().unwrap(SnowflakeConnection.class)
                    .uploadStream(
                            getStageName(),
                            getDestPrefix(quoteType, symbol),
                            inputStream,
                            destFileName,
                            false);
        log.info(String.format("Loaded data to stage '%s' - destPrefix '%s' - destFileName '%s'",
                getStageName(), getDestPrefix(quoteType, symbol), destFileName ) );
        } catch (SQLException e) {
            log.error( String.format(
                    "Could not upload data to stage '%s' (destPrefix '%s' - destFileName '%s')",
                    getStageName(), getDestPrefix(quoteType, symbol), destFileName) );
            throw e;
        }
    }

    private void copyFromStageToTable(String stagePathToFile, String tableName) throws SQLException {
        String copyStgToTableSql = String.format(MERGE_STG_TO_DAILY_TABLE, tableName, stagePathToFile);
        try {
            sfDatasource.getConnection().createStatement().execute(copyStgToTableSql);
            log.info(String.format("Copied data from stage '%s' to table %s.", stagePathToFile, tableName) );

        } catch (SQLException e) {
            log.error(String.format(
                    "SQL Exception while COPYing from stage '%s' to table '%s'.", stagePathToFile, tableName) );
            throw e;
        }
    }

    @SuppressWarnings("unused")
    public static class SnowflakeStorageConfig {
        static final String STAGE_NAME = "SPYTM_QUOTES";
        static final String METADATA_TABLE_NAME = "QUOTES_METADATA";

        static final String MERGE_STG_TO_DAILY_TABLE =
                "MERGE INTO %s m\n" +
                "    USING (SELECT $1 as provider, $2 as quotetype, $3 as symbol, $4 as date,\n" +
                "                  $5 as open, $6 as high, $7 as low, $8 as close, $9 as volume,\n" +
                "                  $10 as adjustedClose, $11 as dividendAmount, $12 as splitCoefficient\n" +
                "            FROM %s\n" +
                "    ) as f\n" +
                "        ON (m.PROVIDER = f.provider AND m.QUOTETYPE = f.quotetype AND m.SYMBOL = f.symbol AND m.date = f.date)\n" +
                "    WHEN NOT MATCHED\n" +
                "        THEN INSERT (provider,quotetype,symbol,date,open,high,low,close,volume,adjustedClose,dividendAmount,splitCoefficient)\n" +
                "        VALUES (provider,quotetype,symbol,date,open,high,low,close,volume,adjustedClose,dividendAmount,splitCoefficient)\n" +
                "    WHEN MATCHED AND m.date = current_date()\n" +
                "        THEN UPDATE SET\n" +
                "            m.open = f.open, m.high = f.high, m.low = f.low, m.close = f.close, m.volume = f.volume,\n" +
                "            m.adjustedClose = f.adjustedClose, m.dividendAmount = f.dividendAmount, m.splitCoefficient = f.splitCoefficient,\n" +
                "            m.insert_ts = current_timestamp()::timestamp_ntz;";

        static final String MERGE_METADATA_SQL = "" +
                "MERGE INTO %s m " +
                "    USING (SELECT v.* FROM VALUES " +
                "                  (?, ?, ?, ?, ?, ?)" +
                "             as v (provider, quotetype, symbol, description, timeZone, lastRefreshed)" +
                // SQL Params           1          2         3         4           5          6
                "    ) as s" +
                "        ON (m.PROVIDER = s.provider AND m.QUOTETYPE = s.quotetype AND m.SYMBOL = s.symbol)" +
                "    WHEN MATCHED" +
                "        THEN UPDATE SET m.LASTREFRESHED = s.lastRefreshed" +
                "    WHEN NOT MATCHED" +
                "        THEN INSERT (provider, quotetype, symbol, description, timeZone, lastRefreshed)" +
                "            VALUES (provider, quotetype, symbol, description, timeZone, lastRefreshed);";

        private static final Random rnd = new Random();

        private SnowflakeStorageConfig() {}

        /**
         * Returns the configured stage name to use for loading data from this application.
         * @return The stage name.
         */
        static String getStageName() {
            return STAGE_NAME;
        }

        /**
         * Builds the destination prefix for the loading of data of a security in a consistent and repeatable way.
         * @param quoteType The type of quote being loaded.
         * @param symbol The symbol of the security being loaded.
         * @return The string to use as prefix of the file in the stage. It is an "absolute" prefix, i.e. has a leading "/".
         */
        static String getDestPrefix(QuoteType quoteType, String symbol) {
            return "/" + quoteType + "/" + symbol;
        }

        /**
         * Generates a new file name at every call.
         *
         * The general format is (date)_(9 digit number).csv
         * @return a file name with .csv extension
         */
        static String getNewCsvFileName() {
            return LocalDate.now() + "_" + String.format("%09d", rnd.nextInt(1000000000)) + ".csv";
        }

        /**
         * The full path for a data file in a stage.
         * @param quoteType The type of quote being loaded.
         * @param symbol The symbol of the security being loaded.
         * @param fileName The name of the file holding the data.
         * @return The full path inside the stage, including the leading "@".
         */
        static String getStagePathToFile(QuoteType quoteType, String symbol, String fileName) {
            return "@" + getStageName() + getDestPrefix(quoteType, symbol) + "/" + fileName;
        }

        /**
         * Maps the quote type to the tabe to hold the data.
         * @param quoteType The type of quote being loaded.
         * @return the name of the table to hold that data.
         */
        static String getTableName(QuoteType quoteType) {
            switch (quoteType) {
                case DAILY:
                case DAILY_ADJUSTED:
                    return "DAILY_QUOTES";
                case WEEKLY:
                case WEEKLY_ADJUSTED:
                    return "WEEKLY_QUOTES";
                case MONTHLY:
                case MONTHLY_ADJUSTED:
                    return "MONTHLY_QUOTES";
                case INTRADAY:
                    return "INTRADAY";
                default:
                    throw new IllegalArgumentException("Not able to get a table name for unknown QuoteType:" + quoteType);
            }
        }
    }
}

