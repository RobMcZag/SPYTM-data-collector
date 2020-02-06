package com.robertozagni.SPYTM.data.collector.downloader;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloader;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.datalake.service.SnowflakeStorageService;
import com.robertozagni.SPYTM.data.collector.service.TimeSerieStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Downloads the required time-serie for the required symbols.
 *
 * app [time-serie] [symbols...]
 */
@Slf4j
@Service
public class StockDataDownloaderRunner implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final TimeSerieStorageService timeSerieStorageService;
    private SnowflakeStorageService snowflakeStorageService;

    @Autowired
    public StockDataDownloaderRunner(
            RestTemplate restTemplate,
            TimeSerieStorageService timeSerieStorageService,
            SnowflakeStorageService snowflakeStorageService) {
        this.restTemplate = restTemplate;
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
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        DownloadConfig cfg = parseArgs(args);

        Map<String, TimeSerie> timeSeries = downloadQuotes(cfg);
        saveTimeSeries(timeSeries);
        // TODO send timeserie to SF - something like below
        // snowflakeStorageService.save(timeSeries);
         snowflakeStorageService.checkConnection();
    }

    private void saveTimeSeries(Map<String, TimeSerie> timeSeries) {
        for (TimeSerie ts:timeSeries.values()) {
            TimeSerie savedQuotesTS = timeSerieStorageService.save(ts);
            log.info(String.format("Saved %d %s quotes for %s from %s!",
                    savedQuotesTS.getData().size(),
                    savedQuotesTS.getMetadata().getQuotetype(),
                    savedQuotesTS.getMetadata().getSymbol(),
                    savedQuotesTS.getMetadata().getProvider() ));
        }
    }

    private Map<String, TimeSerie> downloadQuotes(DownloadConfig cfg) {
        switch (cfg.quoteProvider) {
            case TEST_PROVIDER:
                throw new UnsupportedOperationException("No test service defined at this time!");

            case APLPHA_VANTAGE:
                return new AlphaVantageDownloader(restTemplate).download(cfg.quoteType, cfg.symbols);

            default:
                throw new IllegalArgumentException("Requested unknown quote provider:" + cfg.quoteProvider);
        }
    }

    DownloadConfig parseArgs(String[] args) {
        QuoteType quoteType = QuoteType.DAILY_ADJUSTED;
        QuoteProvider quoteProvider = QuoteProvider.APLPHA_VANTAGE;
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

    private void logDataInfo(TimeSerie ts) {
        log.info("=========");
        log.info(ts.getMetadata().getSymbol());
        log.info(ts.getMetadata().getDescription());
        log.info(ts.getMetadata().getLastRefreshed() + " - " + ts.getMetadata().getTimeZone());
        log.info("--");
        log.info("Rows of stock data: " + ts.getData().size());
        Optional<String> firstDate = ts.getData().keySet().stream().findFirst();
        firstDate.ifPresent(s -> log.info("First day: " + s + " ==> " + ts.getData().get(s)));
        log.info("=========");
    }

    @AllArgsConstructor
     static class DownloadConfig {
        QuoteType quoteType = QuoteType.DAILY_ADJUSTED;
        QuoteProvider quoteProvider = QuoteProvider.APLPHA_VANTAGE;
        List<String> symbols = new ArrayList<>();
    }

}
