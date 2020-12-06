package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerie;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloaderTestHelper;
import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.datalake.service.SnowflakeStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

public class DataCollectorServiceTest {

    @Mock RestTemplate restTemplate;
    @Mock TimeSerieStorageService timeSerieStorageService;
    @Mock SnowflakeStorageService snowflakeStorageService;
    private DataCollectorService downloaderRunner;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        DataDownloaderService dataDownloaderService = new DataDownloaderService(restTemplate);
        downloaderRunner = new DataCollectorService(dataDownloaderService, timeSerieStorageService, snowflakeStorageService);
    }

    @Test
    void download_and_storage_repository_are_invoked_the_right_number_of_times() throws Exception {
        String[] args = {"MSFT", "AAPL", "XYZ"};
        AVTimeSerie msftTimeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_AVTimeSerie();

        when(restTemplate.getForObject(anyString(), eq(AVTimeSerie.class))).thenReturn(msftTimeSerie);
        when(timeSerieStorageService.save(any(TimeSerie.class))).thenReturn(msftTimeSerie.toModel());

        downloaderRunner.run(args);

        verify(restTemplate, times(args.length))
                .getForObject(anyString(), eq(AVTimeSerie.class));

        verify(timeSerieStorageService, times(args.length))
                .save(any(TimeSerie.class));

    }

    @Test
    void parameters_and_symbols_are_parsed_and_passed() {
        String[] args = {"DAILY_ADJUSTED", "MSFT", "AAPL", "APLPHA_VANTAGE"};

        DownloadRequest downloadConfig = DownloadRequest.parseArgs(args);

        assertEquals(downloadConfig.getQuoteProvider(), QuoteProvider.APLPHA_VANTAGE);
        assertEquals(downloadConfig.getQuoteType(), QuoteType.DAILY_ADJUSTED);
        assertIterableEquals(downloadConfig.getSymbols(), Arrays.asList("MSFT", "AAPL"));
    }

    @Test
    void other_parameters_and_symbols_are_parsed_and_passed() {
        String[] args = {"DAILY", "ENI", "AM", "YAHOO_FINANCE"};

        DownloadRequest downloadConfig = DownloadRequest.parseArgs(args);

        assertEquals(downloadConfig.getQuoteProvider(), QuoteProvider.YAHOO_FINANCE);
        assertEquals(downloadConfig.getQuoteType(), QuoteType.DAILY);
        assertIterableEquals(downloadConfig.getSymbols(), Arrays.asList("ENI", "AM"));
    }

    @Test
    void no_parameters_use_default() {
        String[] args = {"MSFT", "AAPL", "XYZ"};

        DownloadRequest downloadConfig = DownloadRequest.parseArgs(args);

        assertEquals(downloadConfig.getQuoteProvider(), DownloadRequest.getDefaultQuoteProvider());
        assertEquals(downloadConfig.getQuoteType(), DownloadRequest.getDefaultQuoteType());
        assertIterableEquals(downloadConfig.getSymbols(), Arrays.asList("MSFT", "AAPL", "XYZ"));

    }

    @Test
    void test_provider_is_recognized() {
        String[] args = {"TEST_PROVIDER", "AAPL", "XYZ"};

        DownloadRequest downloadConfig = DownloadRequest.parseArgs(args);

        assertEquals(downloadConfig.getQuoteProvider(), QuoteProvider.TEST_PROVIDER);
        assertEquals(downloadConfig.getQuoteType(), DownloadRequest.getDefaultQuoteType());

    }

}
