package com.robertozagni.SPYTM.data.datalake.service;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloaderTestHelper;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SnowflakeStorageServiceSpringTest {

    private SnowflakeStorageService snowflakeStorageService;
    private JdbcTemplate sfJdbcTemplate;

    @Autowired
    SnowflakeStorageServiceSpringTest(SnowflakeBasicDataSource sfDataSource, SnowflakeStorageService snowflakeStorageService) {
        this.sfJdbcTemplate = new JdbcTemplate(sfDataSource);
        this.snowflakeStorageService = snowflakeStorageService;
    }


    @Test
    void can_save_a_timeserie() throws IOException {
        if (! snowflakeStorageService.isActive()) return;

        TimeSerie timeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_TimeSerie();

        // Write once and verify
        snowflakeStorageService.load(timeSerie);
        checkTSMetadataIsInSnowflake(timeSerie.getMetadata());
        checkAllTSQuotesAreInSnowflake(timeSerie);

        // Write again and verify no duplication
        snowflakeStorageService.load(timeSerie);
        checkTSMetadataIsInSnowflake(timeSerie.getMetadata());
        checkAllTSQuotesAreInSnowflake(timeSerie);
    }

    private void checkTSMetadataIsInSnowflake(TimeSerieMetadata timeSerieMetadata) {
        ResultSetExtractor<Integer> countResults = rs -> {
            int resultCount = 0;
            while (rs.next()) {
                resultCount++;
            }
            return resultCount;
        };
        Integer countOfResultRows = sfJdbcTemplate.query(selectMetadataByKeySQL(timeSerieMetadata), countResults);
        assertEquals(1, countOfResultRows);
    }

    private String selectMetadataByKeySQL(TimeSerieMetadata timeSerieMetadata) {
        return String.format(
                    "SELECT PROVIDER, QUOTETYPE, SYMBOL, DESCRIPTION, TIMEZONE, LASTREFRESHED " +
                      "FROM SPYTMLAKE_TEST.PUBLIC.QUOTES_METADATA WHERE PROVIDER = '%s' AND QUOTETYPE = '%s' AND SYMBOL = '%s' ;",
                    timeSerieMetadata.getProvider().toString(),
                    timeSerieMetadata.getQuotetype().toString(),
                    timeSerieMetadata.getSymbol()
                    );
    }

    private void checkAllTSQuotesAreInSnowflake(TimeSerie timeSerie) {
        String selectAllQuotesForTimeSerie = String.format(
                "SELECT DATE FROM SPYTMLAKE_TEST.PUBLIC.DAILY_QUOTES WHERE PROVIDER = '%s' AND QUOTETYPE = '%s' AND SYMBOL = '%s' ;",
                timeSerie.getMetadata().getProvider().toString(),
                timeSerie.getMetadata().getQuotetype().toString(),
                timeSerie.getMetadata().getSymbol()
                );
        ResultSetExtractor<Set<String>> checkResults = rs -> {
            Set<String> tsKeys = new HashSet<>(timeSerie.getData().keySet());
            while (rs.next()) {
                tsKeys.remove(rs.getDate("DATE").toLocalDate().toString());
            }
            return tsKeys;
        };
        Set<String> timeSerieKeysNotFound = sfJdbcTemplate.query(selectAllQuotesForTimeSerie, checkResults);
        assertNotNull(timeSerieKeysNotFound);
        assertEquals(0, timeSerieKeysNotFound.size());
    }

    @Test
    void can_save_timeserie_metadata() {
        if (! snowflakeStorageService.isActive()) return;

        RowMapper<TimeSerieMetadata> rowMapper = getTimeSerieMetadataRowMapper();
        TimeSerieMetadata metadata = makeTestTimeSerieMetadata();

        snowflakeStorageService.loadMetadata(metadata);
        List<TimeSerieMetadata> metadataList = sfJdbcTemplate.query(selectMetadataByKeySQL(metadata), rowMapper);
        assertNotNull(metadataList);
        assertEquals(1, metadataList.size());
        assertEquals(metadata, metadataList.get(0));

        metadata.setLastRefreshed(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        snowflakeStorageService.loadMetadata(metadata);
        metadataList = sfJdbcTemplate.query(selectMetadataByKeySQL(metadata), rowMapper);
        assertNotNull(metadataList);
        assertEquals(1, metadataList.size());
        assertEquals(metadata, metadataList.get(0));

    }

    private RowMapper<TimeSerieMetadata> getTimeSerieMetadataRowMapper() {
        return (rs, rowNum) -> {
                TimeSerieMetadata sfMetadata = new TimeSerieMetadata();
                sfMetadata.setProvider(QuoteProvider.valueOf(rs.getString("PROVIDER")));
                sfMetadata.setQuotetype(QuoteType.valueOf(rs.getString("QUOTETYPE")));
                sfMetadata.setSymbol(rs.getString("SYMBOL"));
                sfMetadata.setDescription(rs.getString("DESCRIPTION"));
                sfMetadata.setTimeZone(rs.getString("TIMEZONE"));
                sfMetadata.setLastRefreshed(rs.getString("LASTREFRESHED"));
                return sfMetadata;
            };
    }

    private TimeSerieMetadata makeTestTimeSerieMetadata() {
        String symbol = "METADATA_TEST";
        String description = "Time serie metadata for integration test.";
        String timeZone = "Test/TestPlace";
        String lastRefreshed = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        TimeSerieMetadata metadata = new TimeSerieMetadata();
        metadata.setProvider(QuoteProvider.TEST_PROVIDER);
        metadata.setQuotetype(QuoteType.DAILY);
        metadata.setSymbol(symbol);
        metadata.setDescription(description);
        metadata.setTimeZone(timeZone);
        metadata.setLastRefreshed(lastRefreshed);
        return metadata;
    }
}