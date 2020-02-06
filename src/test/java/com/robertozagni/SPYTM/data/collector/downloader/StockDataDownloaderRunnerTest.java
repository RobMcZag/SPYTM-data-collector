package com.robertozagni.SPYTM.data.collector.downloader;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerie;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloaderTestHelper;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.datalake.service.SnowflakeStorageService;
import com.robertozagni.SPYTM.data.collector.service.TimeSerieStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockDataDownloaderRunnerTest {
    @Mock RestTemplate restTemplate;
    @Mock TimeSerieStorageService timeSerieStorageService;
    @Mock SnowflakeStorageService snowflakeStorageService;
    private StockDataDownloaderRunner downloaderRunner;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        downloaderRunner = new StockDataDownloaderRunner(restTemplate, timeSerieStorageService, snowflakeStorageService);
    }

    @Test
    void download_and_storage_repository_are_invoked() throws Exception {
        String[] args = {"MSFT", "AAPL", "XYZ"};
        AVTimeSerie msftTimeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_AVTimeSerie();

        when(restTemplate.getForObject(anyString(), eq(AVTimeSerie.class))).thenReturn(msftTimeSerie);
        when(timeSerieStorageService.save(any(TimeSerie.class))).thenReturn(msftTimeSerie.toModel());

        downloaderRunner.run(args);

        verify(restTemplate, times(args.length)).getForObject(anyString(), eq(AVTimeSerie.class));
        verify(timeSerieStorageService, times(args.length)).save(any(TimeSerie.class));

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
