package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.downloader.Downloader;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloader;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Service
public class DataDownloaderService {
    private final RestTemplate restTemplate;

    @Autowired
    public DataDownloaderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Downloads the timeseries requested in the passed configuration.
     *
     * The provider is used to build the correct downloader and then the downloader is invoked with the
     * quote type and the list of symbols to download.
     *
     * Note: the results contains only symbols for which data was available.
     *
     * @param cfg the configuration holding the desired provider, time serie type and list of symbols
     * @return a map with symbols as keys and a TimeSerie with the downloaded data as value
     */
    public Map<String, TimeSerie> downloadQuotes(@NotNull DownloadRequest cfg) {
        return getDownloader(cfg.getQuoteProvider())
                .download(cfg.getQuoteType(), cfg.getSymbols());
    }

    Downloader getDownloader(QuoteProvider quoteProvider) {
        if (quoteProvider == null) {
            quoteProvider = DownloadRequest.getDefaultQuoteProvider();
        }
        switch (quoteProvider) {
            case TEST_PROVIDER:
                throw new UnsupportedOperationException("No test provider defined at this time!");

            case APLPHA_VANTAGE:
                return new AlphaVantageDownloader(restTemplate);

            default:
                throw new IllegalArgumentException("Requested unknown quote provider:" + quoteProvider);
        }
    }

}
