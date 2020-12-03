package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.robertozagni.SPYTM.data.collector.downloader.Downloader;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerie;
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

    private static final String COOKIE = "222hgfpfjstl9&b=3&s=h0";
    private static final @Getter String CRUMB = "3Vcqzbla\u002FWd"; //"CrumbStore":{"crumb":"3Vcqzbla\u002FWd"} | (U+002F) is Unicode Character 'SOLIDUS'

    private final RestTemplate restTemplate;

    public YFv8StockDownloader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, TimeSerie> download(DownloadRequest downloadRequest) {
        Map<String, TimeSerie> result = new HashMap<>();
        if (downloadRequest.getSymbols() == null) { return result; }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "B="+COOKIE );

        for (String symbol: downloadRequest.getSymbols()) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL_HTTP11 + PRICE_URL_TEMPLATE + symbol)
                    .queryParam("symbol", symbol)
                    .queryParam("period1", 0)
                    .queryParam("period2", 999999999)
                    .queryParam("interval", "1d")
                    .queryParam("crumb", CRUMB)
                    ;
            String url = builder.toUriString();

            log.debug("** Downloading " + url);
            try {
                ResponseEntity<YFv8TimeSerie> response = restTemplate.exchange(
                                                            url,
                                                            HttpMethod.GET,
                                                            new HttpEntity<String>(headers),
                                                            YFv8TimeSerie.class);
                YFv8TimeSerie data = response.getBody();
                assert data != null;
//                if (data.getMetadata() == null) { continue; }   // We have nothing to say for this symbol
//                result.put(symbol, AVTimeSerie.toModel(data));

            } catch (RestClientException e) {
                log.error("Error while attempting to download data from " + url);
                log.error(e.toString());
            }
        }
        return result;
    }
}
