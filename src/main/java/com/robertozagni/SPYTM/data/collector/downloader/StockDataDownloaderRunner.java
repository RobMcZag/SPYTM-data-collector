package com.robertozagni.SPYTM.data.collector.downloader;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloader;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.service.DailyQuoteStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
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
public class StockDataDownloaderRunner implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final DailyQuoteStorageService dailyQuoteStorageService;

    public StockDataDownloaderRunner(RestTemplate restTemplate, DailyQuoteStorageService dailyQuoteStorageService) {
        this.restTemplate = restTemplate;
        this.dailyQuoteStorageService = dailyQuoteStorageService;
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

        Map<String, TimeSerie> timeSeries;

        switch (cfg.quoteProvider) {
            case TEST_PROVIDER:
                throw new UnsupportedOperationException("No test service defined at this time!");

            case APLPHA_VANTAGE:
            default:
                timeSeries = new AlphaVantageDownloader(restTemplate).download(cfg.quoteType, cfg.symbols);
                break;
        }

        for (String symbol:timeSeries.keySet()) {
            TimeSerie ts = timeSeries.get(symbol);
            logDataInfo(ts);
            TimeSerie savedQuotesTS = dailyQuoteStorageService.saveAllQuotes(ts);
            log.info(String.format("Saved %d quotes!", savedQuotesTS.getData().size()));
        }
    }

    DownloadConfig parseArgs(String[] args) {
        QuoteType quoteType = QuoteType.DAILY_ADJUSTED;
        QuoteProvider quoteProvider = QuoteProvider.APLPHA_VANTAGE;
        List<String> symbols = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i < 2) {
                try {
                    quoteType = QuoteType.valueOf(arg);
                    continue;
                } catch (IllegalArgumentException ignored) { }   // Not a serie type
                try {
                    quoteProvider = QuoteProvider.valueOf(arg);
                    continue;
                } catch (IllegalArgumentException ignored) { }   // Not a provider
            }
            symbols.add(arg);
        }
        return new DownloadConfig(quoteType, quoteProvider, symbols);
    }

    private void logDataInfo(TimeSerie ts) {
        log.info("=========");
        log.info(ts.getMetadata().getSeriesInfo());
        log.info(ts.getMetadata().getSymbol());
        log.info(ts.getMetadata().getLastRefreshed() + " - " + ts.getMetadata().getTimeZone());
        log.info(ts.getMetadata().getOutputSize());
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
