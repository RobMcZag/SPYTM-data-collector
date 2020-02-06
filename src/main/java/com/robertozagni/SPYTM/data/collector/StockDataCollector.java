package com.robertozagni.SPYTM.data.collector;

import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.service.StockDataDownloaderService;
import com.robertozagni.SPYTM.data.datalake.service.SnowflakeStorageService;
import com.robertozagni.SPYTM.data.collector.service.TimeSerieStorageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple runner to download, store locally and push to the data lake quote data for some stock symbols.
 *
 * Downloads the requested time-serie for the requested symbols.
 * Stores the downloaded TimeSeries in the local DB.
 * Pushes the data to the data lake.
 *
 * command line arguments: [QuoteType] [QuoteProvider] Symbol1 Symbol2 ...
 * All passed arguments are parsed to check if they identify a QuoteType or QuoteProvider to use.
 * All arguments not identifying a QuoteType or QuoteProvider is considered a security symbol to download data for.
 *
 */
@Slf4j
@Service
public class StockDataCollector implements CommandLineRunner {

    private final StockDataDownloaderService stockDataDownloaderService;
    private final TimeSerieStorageService timeSerieStorageService;
    private SnowflakeStorageService snowflakeStorageService;

    @Autowired
    public StockDataCollector(
            StockDataDownloaderService stockDataDownloaderService,
            TimeSerieStorageService timeSerieStorageService,
            SnowflakeStorageService snowflakeStorageService) {
        this.stockDataDownloaderService = stockDataDownloaderService;
        this.timeSerieStorageService = timeSerieStorageService;
        this.snowflakeStorageService = snowflakeStorageService;
    }

    /**
     * Collect and parse the arguments passed, invoking the right downloader.
     * @param args incoming main method arguments<BR>
     * Example: <pre>command TIME_SERIES_DAILY_ADJUSTED MSFT AAPL BA </pre>
     * will retrieve "daily adjusted data" for Microsoft, Apple and Boeing <BR>
     * TIME_SERIES_DAILY_ADJUSTED is the name of the desired data series, that also picks the data provider; <BR>
     * MSFT AAPL BA exemplify one or more stock symbols;
     */
    @Override
    public void run(String... args) {

        DownloadConfig cfg = parseArgs(args);

        Map<String, TimeSerie> timeSeries = stockDataDownloaderService.downloadQuotes(cfg);
        timeSeries.values().forEach(
                (TimeSerie serie) ->
                    log.info(String.format("Downloaded %d %s quotes for %s from %s!",
                            serie.getData().size(),
                            serie.getMetadata().getQuotetype(),
                            serie.getMetadata().getSymbol(),
                            serie.getMetadata().getProvider()) )
        );

        timeSeries.values().forEach(
                (TimeSerie serie) -> {
                    timeSerieStorageService.save(serie);
                    log.info(String.format("Downloaded %d %s quotes for %s from %s!",
                            serie.getData().size(),
                            serie.getMetadata().getQuotetype(),
                            serie.getMetadata().getSymbol(),
                            serie.getMetadata().getProvider()) );
                }
        );

        // TODO send timeserie to SF - something like below
        // snowflakeStorageService.save(timeSeries);
         snowflakeStorageService.checkConnection();
    }

    DownloadConfig parseArgs(String[] args) {
        QuoteType quoteType = stockDataDownloaderService.getDefaultQuoteType();
        QuoteProvider quoteProvider = stockDataDownloaderService.getDefaultQuoteProvider();
        List<String> symbols = new ArrayList<>();

        for (String arg: args) {
            try {
                quoteType = QuoteType.valueOf(arg);
                continue;
            } catch (IllegalArgumentException ignored) { }   // Not a serie type
            try {
                quoteProvider = QuoteProvider.valueOf(arg);
                continue;
            } catch (IllegalArgumentException ignored) { }   // Not a provider

            symbols.add(arg);                                // Then it's a symbol ! :)
        }
        return new DownloadConfig(quoteType, quoteProvider, symbols);
    }

    @AllArgsConstructor @Getter
    public static class DownloadConfig {
        private QuoteType quoteType;
        private QuoteProvider quoteProvider;
        List<String> symbols;
    }

}
