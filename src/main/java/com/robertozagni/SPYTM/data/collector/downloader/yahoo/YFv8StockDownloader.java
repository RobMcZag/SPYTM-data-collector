package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.robertozagni.SPYTM.data.collector.downloader.Downloader;
import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.model.DownloadSize;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class YFv8StockDownloader implements Downloader {

    private static final @Getter String BASE_URL_HTTP11 = "https://query2.finance.yahoo.com";  // HTTP/1.1
    private static final @Getter String PRICE_URL_TEMPLATE = "/v8/finance/chart/"; // "/v8/finance/chart/AAPL?symbol=AAPL&period1=0&period2=9999999999&interval=3mo";

    /**
     *  Number of days to retrieve when DownloadSize = LATEST. Should be <= 80 so that works for any granularity.
     */
    public static final int DAYS_FOR_LATEST = 60;

    private final RestTemplate restTemplate;

    public YFv8StockDownloader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, TimeSerie> download(DownloadRequest downloadRequest) {
        Map<String, TimeSerie> result = new HashMap<>();
        if (downloadRequest.getSymbols() == null) { return result; }

        QuoteType quoteType = downloadRequest.getQuoteType();
        for (String symbol: downloadRequest.getSymbols()) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL_HTTP11 + PRICE_URL_TEMPLATE + symbol)
                    .queryParam("symbol", symbol)
                    .queryParam("period1", getStartPeriod(downloadRequest))
                    .queryParam("period2", getEndPeriod(downloadRequest))
                    .queryParam("interval", YFv8TimeSerie.YFStockResultMeta.getGranularity(quoteType))
                    .queryParam("events", "div,split")
                    ;
            String url = builder.toUriString();

            log.debug("** Downloading " + url);
            try {
                YFv8TimeSerie data = restTemplate.getForObject(url, YFv8TimeSerie.class);
                assert data != null;
                result.put(symbol, YFv8TimeSerie.toModel(data));

            } catch (RestClientException e) {
                log.error("Error while attempting to download data from " + url);
                log.error(e.toString());
            }
        }
        return result;
    }

    private long getStartPeriod(DownloadRequest downloadRequest) {
        if (downloadRequest.getDownloadSize() == DownloadSize.FULL) {
            return 0;
        }
        return Instant.now()
                .truncatedTo(ChronoUnit.DAYS)
                .minus(Duration.ofDays(DAYS_FOR_LATEST))
                .getEpochSecond();
    }
    private long getEndPeriod(DownloadRequest downloadRequest) {
        if (downloadRequest.getDownloadSize() == DownloadSize.FULL) {
            return Instant.MAX.getEpochSecond();
        }
        return Instant.now()
                .truncatedTo(ChronoUnit.DAYS)
                .plus(Duration.ofDays(1))   // ad one, just in case :)
                .getEpochSecond();
    }
}
