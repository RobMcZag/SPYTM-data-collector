package com.robertozagni.SPYTM.data.collector;

import com.robertozagni.SPYTM.data.collector.model.alphavantage.TimeSerieData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Downloads the required time-serie for the required symbols.
 *
 * app [time-serie] [symbols...]
 */
@Slf4j
public class StockDataDownloader implements CommandLineRunner {
    private static final String timeSerie = "TIME_SERIES_DAILY_ADJUSTED";
    private final RestTemplate restTemplate;

    public StockDataDownloader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=MSFT&apikey=demo";
        String timeSerie = "TIME_SERIES_DAILY_ADJUSTED";
        String symbol = "MSFT";

        // TODO Check passed arguments: set timeserie and loop on given symbols
        TimeSerieData data = restTemplate.getForObject(url, TimeSerieData.class);

        log.info(data.toString());
    }
}
