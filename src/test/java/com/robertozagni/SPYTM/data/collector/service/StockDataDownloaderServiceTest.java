package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.downloader.Downloader;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloader;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class StockDataDownloaderServiceTest {

    @Mock RestTemplate mockRestTemplate;
    private StockDataDownloaderService stockDataDownloaderService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        stockDataDownloaderService = new StockDataDownloaderService(mockRestTemplate);
    }

    @Test
    void default_provider_is_used_if_null_is_passed() {
        Downloader downloader = stockDataDownloaderService.getDownloader(null);

        assertNotNull(downloader);

        assert stockDataDownloaderService.getDefaultQuoteProvider() == QuoteProvider.APLPHA_VANTAGE;
        assertTrue(downloader instanceof AlphaVantageDownloader);
    }
    @Test
    void AV_provider_is_used_if_AV_is_passed() {
        Downloader downloader = stockDataDownloaderService.getDownloader(QuoteProvider.APLPHA_VANTAGE);

        assertNotNull(downloader);
        assertTrue(downloader instanceof AlphaVantageDownloader);
    }
    @Test
    void TEST_provider_generates_exception() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> stockDataDownloaderService.getDownloader(QuoteProvider.TEST_PROVIDER)
        );

    }
}