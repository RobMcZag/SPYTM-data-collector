package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.DataSeries;
import com.robertozagni.SPYTM.data.collector.model.alphavantage.TimeSerieData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Slf4j
public class AlphaVantageDownloader {
    // TODO collect APIKey from config, ideally from env (not typed into config or source)
    private static final String API_KEY = "SHGHQ9MPDSLJSKO5";
    private static final String BASE_URL = "https://www.alphavantage.co/query";

    private final RestTemplate restTemplate;

    public AlphaVantageDownloader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void download(DataSeries dataSeries, List<String> symbols) {
        //String timeSerie = "TIME_SERIES_DAILY_ADJUSTED";
        //String symbol = "MSFT";

        for (String symbol: symbols) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("function", dataSeries.name())
                    .queryParam("symbol", symbol)
                    .queryParam("apikey", API_KEY);
            String url = builder.toUriString();

            log.info("** Downloading " + url);
            TimeSerieData data = restTemplate.getForObject(url, TimeSerieData.class);
            assert data != null;
            logDataInfo(data);
        }

    }

    private void logDataInfo(TimeSerieData data) {
        log.info("=========");
        log.info(data.getMetadata().getSeriesInfo());
        log.info(data.getMetadata().getSymbol());
        log.info(data.getMetadata().getLastRefreshed() + " - " + data.getMetadata().getTimeZone());
        log.info(data.getMetadata().getOutputSize());
        log.info("--");
        log.info("Rows of stock data: " + String.valueOf(data.getStockData().size()));
        Optional<String> firstDate = data.getStockData().keySet().stream().findFirst();
        firstDate.ifPresent(s -> log.info("First day: " + s + " ==> " + data.getStockData().get(s)));
        log.info("=========");
    }
}
