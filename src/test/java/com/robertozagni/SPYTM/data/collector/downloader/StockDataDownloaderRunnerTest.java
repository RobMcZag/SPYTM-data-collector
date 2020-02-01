package com.robertozagni.SPYTM.data.collector.downloader;

import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.service.DailyQuoteStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockDataDownloaderRunnerTest {
    @Mock RestTemplate restTemplate;
    @Mock DailyQuoteStorageService storageService;
    StockDataDownloaderRunner downloaderRunner;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        downloaderRunner = new StockDataDownloaderRunner(restTemplate, storageService);
    }

    @Test
    void dataSerie_and_symbols_are_parsed_and_passed() throws Exception {
        String[] args = {"DAILY_ADJUSTED", "MSFT", "AAPL"};

        StockDataDownloaderRunner.DownloadConfig downloadConfig = downloaderRunner.parseArgs(args);

        assertEquals(downloadConfig.quoteProvider, QuoteProvider.APLPHA_VANTAGE);
        assertEquals(downloadConfig.quoteType, QuoteType.DAILY_ADJUSTED);
        assertIterableEquals(downloadConfig.symbols, Arrays.asList("MSFT", "AAPL"));
    }

    @Test
    void no_dataSerie_uses_default() throws Exception {
        String[] args = {"MSFT", "AAPL", "XYZ"};

        StockDataDownloaderRunner.DownloadConfig downloadConfig = downloaderRunner.parseArgs(args);

        assertEquals(downloadConfig.quoteProvider, QuoteProvider.APLPHA_VANTAGE);
        assertEquals(downloadConfig.quoteType, QuoteType.DAILY_ADJUSTED);
        assertIterableEquals(downloadConfig.symbols, Arrays.asList("MSFT", "AAPL", "XYZ"));

    }

    @Test
    void test_provider_is_recognized() {
        String[] args = {"TEST_PROVIDER", "AAPL", "XYZ"};

        StockDataDownloaderRunner.DownloadConfig downloadConfig = downloaderRunner.parseArgs(args);

        assertEquals(downloadConfig.quoteProvider, QuoteProvider.TEST_PROVIDER);
        assertEquals(downloadConfig.quoteType, QuoteType.DAILY_ADJUSTED);

    }

}
