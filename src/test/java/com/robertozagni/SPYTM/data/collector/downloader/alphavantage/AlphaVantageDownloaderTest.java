package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlphaVantageDownloaderTest {

    @Mock private RestTemplate mockRestTemplate;

    private AlphaVantageDownloader downloader;

    @BeforeEach
    void setUp() {
        downloader = new AlphaVantageDownloader(mockRestTemplate);
    }

    @Test
    void process_correctly_one_time_serie() throws IOException {
        List<String> msft = Collections.singletonList("MSFT");
        AVTimeSerie msftAVTimeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_AVTimeSerie();

        when(mockRestTemplate.getForObject(contains("&symbol=MSFT&"), eq(AVTimeSerie.class))).thenReturn(msftAVTimeSerie);

        DownloadRequest downloadRequest = new DownloadRequest(
                QuoteType.DAILY,
                QuoteProvider.ALPHA_VANTAGE,
                DownloadRequest.getDefaultDownloadSize(),
                msft
        );
        Map<String, TimeSerie> timeSerieMap = downloader.download(downloadRequest);

        verify(mockRestTemplate, times(msft.size())).getForObject(anyString(), eq(AVTimeSerie.class));

        assertEquals(1, timeSerieMap.keySet().size());
        assertTrue(timeSerieMap.containsKey("MSFT"));
        assertEquals(timeSerieMap.get("MSFT"), msftAVTimeSerie.toModel());
    }

    @Test
    void process_correctly_with_missing_time_serie() throws IOException {
        List<String> symbols = Arrays.asList("ABCX", "MSFT", "XYZ");
        AVTimeSerie msftAVTimeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_AVTimeSerie();
        AVTimeSerie emptyAVTimeSerie = new AVTimeSerie();

        when(mockRestTemplate.getForObject(anyString(), eq(AVTimeSerie.class))).thenReturn(emptyAVTimeSerie);
        when(mockRestTemplate.getForObject(contains("&symbol=MSFT&"), eq(AVTimeSerie.class))).thenReturn(msftAVTimeSerie);

        DownloadRequest downloadRequest = new DownloadRequest(
                QuoteType.DAILY,
                QuoteProvider.ALPHA_VANTAGE,
                DownloadRequest.getDefaultDownloadSize(),
                symbols
        );
        Map<String, TimeSerie> timeSerieMap = downloader.download(downloadRequest);

        verify(mockRestTemplate, times(symbols.size())).getForObject(anyString(), eq(AVTimeSerie.class));

        assertEquals(1, timeSerieMap.keySet().size());
        assertTrue(timeSerieMap.containsKey("MSFT"));
        assertEquals(timeSerieMap.get("MSFT"), msftAVTimeSerie.toModel());
    }
    @Test
    void return_empty_timeserie_when_null_is_passed_for_symbol() {
        DownloadRequest downloadRequest = new DownloadRequest(
                QuoteType.DAILY,
                QuoteProvider.ALPHA_VANTAGE,
                DownloadRequest.getDefaultDownloadSize(),
                null
        );
        Map<String, TimeSerie> noDataExpected = downloader.download(downloadRequest);
        assertNotNull(noDataExpected);
        assertTrue(noDataExpected.isEmpty());
    }

    @Test
    void return_empty_timeserie_when_no_simbol_is_passed() {
        DownloadRequest downloadRequest = new DownloadRequest(
                QuoteType.DAILY,
                QuoteProvider.ALPHA_VANTAGE,
                DownloadRequest.getDefaultDownloadSize(),
                new ArrayList<>()
        );
        Map<String, TimeSerie> noDataExpected = downloader.download(downloadRequest);
        assertNotNull(noDataExpected);
        assertTrue(noDataExpected.isEmpty());
    }

    @Test
    void return_empty_timeserie_when_there_is_an_error_in_rest_call() {
        when(mockRestTemplate.getForObject(anyString(), eq(AVTimeSerie.class)))
                .thenThrow(new RestClientException("Sorry, I am just a mock ! I can not retrieve data for you"));

        DownloadRequest downloadRequest = new DownloadRequest(
                QuoteType.DAILY,
                QuoteProvider.ALPHA_VANTAGE,
                DownloadRequest.getDefaultDownloadSize(),
                Collections.singletonList("MSFT")
        );
        Map<String, TimeSerie> noDataExpected = downloader.download(downloadRequest);
        assertNotNull(noDataExpected);
        assertTrue(noDataExpected.isEmpty());
    }

}