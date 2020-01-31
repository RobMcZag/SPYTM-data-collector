package com.robertozagni.SPYTM.data.collector.downloader;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloader;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
    private static final String timeSerie = "TIME_SERIES_DAILY_ADJUSTED";

    private final AlphaVantageDownloader alphaVantageDownloader;

    public StockDataDownloaderRunner(AlphaVantageDownloader alphaVantageDownloader) {
        this.alphaVantageDownloader = alphaVantageDownloader;
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


        switch (quoteProvider) {
            case TEST_PROVIDER:
                throw new UnsupportedOperationException("No test service defined at this time");

            case APLPHA_VANTAGE:
            default:
                Map<String, TimeSerie> timeSeries = alphaVantageDownloader.download(quoteType, symbols);
                for (String symbol:timeSeries.keySet()) {
                    logDataInfo(timeSeries.get(symbol));
                }
                break;
        }

    }

    private void logDataInfo(TimeSerie ts) {
        log.info("=========");
        log.info(ts.getMetadata().getSeriesInfo());
        log.info(ts.getMetadata().getSymbol());
        log.info(ts.getMetadata().getLastRefreshed() + " - " + ts.getMetadata().getTimeZone());
        log.info(ts.getMetadata().getOutputSize());
        log.info("--");
        log.info("Rows of stock data: " + String.valueOf(ts.getData().size()));
        Optional<String> firstDate = ts.getData().keySet().stream().findFirst();
        firstDate.ifPresent(s -> log.info("First day: " + s + " ==> " + ts.getData().get(s)));
        log.info("=========");
    }

}
