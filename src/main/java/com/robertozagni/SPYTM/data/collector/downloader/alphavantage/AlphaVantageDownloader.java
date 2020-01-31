package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.*;
import com.robertozagni.SPYTM.data.collector.model.alphavantage.AVTimeSerie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AlphaVantageDownloader implements com.robertozagni.SPYTM.data.collector.downloader.Downloader {
    // TODO collect APIKey from config, ideally from env (not typed into config or source)
    private static final String API_KEY = "SHGHQ9MPDSLJSKO5";

    private static final String BASE_URL = "https://www.alphavantage.co/query";

    private final RestTemplate restTemplate;

    public AlphaVantageDownloader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, TimeSerie> download(QuoteType quoteType, List<String> symbols) {
        Map<String, TimeSerie> result = new HashMap<>();
        if (symbols == null) { return result; }

        for (String symbol: symbols) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("function", getFunctionName(quoteType))
                    .queryParam("symbol", symbol)
                    .queryParam("apikey", API_KEY);
            String url = builder.toUriString();

            log.debug("** Downloading " + url);
            try {

                AVTimeSerie data = restTemplate.getForObject(url, AVTimeSerie.class);
                assert data != null;
                if (data.getMetadata() == null) { continue; }   // We have nothing to say for this symbol
                if (data.getAvQuotes() == null) { data.setAvQuotes(new HashMap<>()); }  // No quotes is fine
                result.put(symbol, AVTimeSerie.toModel(data));

            } catch (RestClientException e) {
                log.error("Error while attempting to download data from " + url);
                log.error(e.toString());
            }
        }
        return result;

    }

    private String getFunctionName(QuoteType quoteType) {
        // TODO make some more resilient mapper :)
        return "TIME_SERIES_" + (quoteType==null ? "" : quoteType.name());
    }

}