package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.model.DownloadSize;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AlphaVantageDownloader implements com.robertozagni.SPYTM.data.collector.downloader.Downloader {
    // TODO collect APIKey from config, ideally from env (not typed into config or source)
    private static final String API_KEY = "SHGHQ9MPDSLJSKO5";

    private static final String BASE_URL = "https://www.alphavantage.co/query";

    private static final int NUM_CALL_PER_MINUTE = 5;   // Max 5 call per minute - 500 per day
    private static final int MIN_SECS_BETWEEN_API_CALL = 60 / NUM_CALL_PER_MINUTE;

    private final RestTemplate restTemplate;

    public AlphaVantageDownloader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, TimeSerie> download(DownloadRequest downloadRequest) {
        Map<String, TimeSerie> result = new HashMap<>();
        if (downloadRequest.getSymbols() == null) { return result; }

        for (String symbol: downloadRequest.getSymbols()) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("function", getFunctionName(downloadRequest.getQuoteType()))
                    .queryParam("symbol", symbol)
                    .queryParam("outputsize", getOutputSize(downloadRequest.getDownloadSize()))
                    .queryParam("apikey", API_KEY);
            String url = builder.toUriString();

            log.debug("** Downloading " + url);
            try {

                AVTimeSerie data = restTemplate.getForObject(url, AVTimeSerie.class);
                assert data != null;
                if (data.getMetadata() == null) { continue; }   // We have nothing to say for this symbol
                result.put(symbol, AVTimeSerie.toModel(data));

            } catch (RestClientException e) {
                log.error("Error while attempting to download data from " + url);
                log.error(e.toString());
            }

            if (downloadRequest.getSymbols().size() > NUM_CALL_PER_MINUTE) {
                try {
                    log.info("Waiting "+MIN_SECS_BETWEEN_API_CALL+" secs after downloading " + symbol + ".");
                    TimeUnit.SECONDS.sleep(MIN_SECS_BETWEEN_API_CALL);
                } catch (InterruptedException ignored) {
                    log.info("Interrupted earlier than expected...");
                }
            }
        }
        return result;

    }

    /**
     * Returns the string representing the required download size for this downloader.
     *
     * Strings "compact" and "full" are accepted with the following specifications:
     * - compact returns only the latest 100 data points;
     * - full returns the full-length time series of 20+ years of historical data.
     */
    private String getOutputSize(DownloadSize downloadSize) {
        if (downloadSize == null) {
            throw new IllegalArgumentException("Download Size can not be null.");
        }
        switch (downloadSize) {
            case FULL:
                return "full";

            default:
            case LATEST:
                return "compact";
        }
    }

    private String getFunctionName(QuoteType quoteType) {
        if (quoteType == null) {
            throw new IllegalArgumentException("Quote provider can not be null.");
        }
        switch (quoteType) {
            case INTRADAY:
                throw new UnsupportedOperationException("Download of Intraday Quotes not yet implemented");

            case DAILY:
                return "TIME_SERIES_DAILY";
            case DAILY_ADJUSTED:
                return "TIME_SERIES_DAILY_ADJUSTED";
            case WEEKLY:
                return "TIME_SERIES_WEEKLY";
            case WEEKLY_ADJUSTED:
                return "TIME_SERIES_WEEKLY_ADJUSTED";
            case MONTHLY:
                return "TIME_SERIES_MONTHLY";
            case MONTHLY_ADJUSTED:
                return "TIME_SERIES_MONTHLY_ADJUSTED";

            default:
                return "TIME_SERIES_" + quoteType.name();
        }
    }

}
