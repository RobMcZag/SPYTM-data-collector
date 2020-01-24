package com.robertozagni.SPYTM.data.collector;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Downloads the required time-serie for the required symbols.
 *
 * app [time-serie] [symbols...]
 */
@Slf4j
public class StockDataDownloader implements CommandLineRunner {
    private static final String timeSerie = "TIME_SERIES_DAILY_ADJUSTED";

    private final AlphaVantageDownloader alphaVantageDownloader;

    public StockDataDownloader(AlphaVantageDownloader alphaVantageDownloader) {
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
        DataServices dataService;
        List<String> symbols = new ArrayList<>();
        DataSeries dataSeries = DataSeries.TIME_SERIES_DAILY_ADJUSTED;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i == 0) {
                try {
                    dataSeries = DataSeries.valueOf(arg);
                    continue;
                } catch (IllegalArgumentException ignored) { }   // Not a series, then it is a symbol :)
            }
            symbols.add(arg);
        }

        dataService = dataSeries.getDatService();

        switch (dataService) {
            case TEST:
                throw new UnsupportedOperationException("No test service defined at this time");

            case APLPHA_VANTAGE:
            default:
                alphaVantageDownloader.download(dataSeries, symbols);
                break;
        }

    }
}
