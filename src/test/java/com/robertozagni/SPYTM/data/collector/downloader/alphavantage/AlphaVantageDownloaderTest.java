package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.alphavantage.AVTimeSerie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

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
    void return_empty_timeserie_when_null_is_passed_for_symbol() {
        Map<String, TimeSerie> noDataExpected = downloader.download(QuoteType.DAILY, null);
        assertNotNull(noDataExpected);
        assertTrue(noDataExpected.isEmpty());
    }

    @Test
    void return_empty_timeserie_when_no_simbol_is_passed() {
        Map<String, TimeSerie> noDataExpected = downloader.download(null, new ArrayList<>());
        assertNotNull(noDataExpected);
        assertTrue(noDataExpected.isEmpty());
    }

    @Test
    void return_empty_timeserie_when_there_is_an_error_in_rest_call() {
        when(mockRestTemplate.getForObject(anyString(), eq(AVTimeSerie.class)))
                .thenThrow(new RestClientException("Sorry, I am just a mock ! I can not retrieve data for you"));

        Map<String, TimeSerie> noDataExpected = downloader.download(QuoteType.DAILY, Collections.singletonList("MSFT"));
        assertNotNull(noDataExpected);
        assertTrue(noDataExpected.isEmpty());
    }

}