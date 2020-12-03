package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.robertozagni.SPYTM.data.collector.downloader.Downloader;
import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class YFv8StockDownloader implements Downloader {

    private static final String BASE_URL_HTTP10 = "https://query1.finance.yahoo.com";  // HTTP/1.0
    private static final @Getter String BASE_URL_HTTP11 = "https://query2.finance.yahoo.com";  // HTTP/1.1
    private static final @Getter String PRICE_URL_TEMPLATE = "/v8/finance/chart/"; // "/v8/finance/chart/AAPL?symbol=AAPL&period1=0&period2=9999999999&interval=3mo";

    private final RestTemplate restTemplate;

    public YFv8StockDownloader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, TimeSerie> download(DownloadRequest downloadRequest) {
        Map<String, TimeSerie> result = new HashMap<>();
        if (downloadRequest.getSymbols() == null) { return result; }

        for (String symbol: downloadRequest.getSymbols()) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL_HTTP11 + PRICE_URL_TEMPLATE + symbol)
                    .queryParam("symbol", symbol)
                    .queryParam("period1", 0)
                    .queryParam("period2", 999999999)
                    .queryParam("interval", "1d")
                    ;
            String url = builder.toUriString();

            log.debug("** Downloading " + url);
            try {
                YFv8TimeSerie data = restTemplate.getForObject(url, YFv8TimeSerie.class);
                assert data != null;
//                if (data.getMetadata() == null) { continue; }   // We have nothing to say for this symbol
                result.put(symbol, YFv8TimeSerie.toModel(data));

            } catch (RestClientException e) {
                log.error("Error while attempting to download data from " + url);
                log.error(e.toString());
            }
        }
        return result;
    }
}
