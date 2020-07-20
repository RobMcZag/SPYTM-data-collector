package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.downloader.Downloader;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloader;
import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class DataDownloaderServiceTest {

    @Mock RestTemplate mockRestTemplate;
    private DataDownloaderService dataDownloaderService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        dataDownloaderService = new DataDownloaderService(mockRestTemplate);
    }

    @Test
    void default_provider_is_used_if_null_is_passed() {
        Downloader downloader = dataDownloaderService.getDownloader(null);

        assertNotNull(downloader);

        assert DownloadRequest.getDefaultQuoteProvider() == QuoteProvider.APLPHA_VANTAGE;
        assertTrue(downloader instanceof AlphaVantageDownloader);
    }
    @Test
    void AV_provider_is_used_if_AV_is_passed() {
        Downloader downloader = dataDownloaderService.getDownloader(QuoteProvider.APLPHA_VANTAGE);

        assertNotNull(downloader);
        assertTrue(downloader instanceof AlphaVantageDownloader);
    }
    @Test
    void TEST_provider_generates_exception() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> dataDownloaderService.getDownloader(QuoteProvider.TEST_PROVIDER)
        );

    }
}