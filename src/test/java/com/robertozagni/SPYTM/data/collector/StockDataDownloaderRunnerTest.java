package com.robertozagni.SPYTM.data.collector;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloader;
import com.robertozagni.SPYTM.data.collector.model.DataSerie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockDataDownloaderRunnerTest {
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    @Mock AlphaVantageDownloader mockAVDownloader;

    @Test
    void dataSerie_and_symbols_are_parsed_and_passed() throws Exception {
        ArgumentCaptor<List<String>> symbols = ArgumentCaptor.forClass(List.class);

        StockDataDownloaderRunner downloader = new StockDataDownloaderRunner(mockAVDownloader);
        downloader.run("TIME_SERIES_DAILY_ADJUSTED", "MSFT", "AAPL");

        verify(mockAVDownloader, times(1))
                .download(eq(DataSerie.TIME_SERIES_DAILY_ADJUSTED), symbols.capture());

        assertIterableEquals(symbols.getValue(), Arrays.asList("MSFT", "AAPL"));

    }

    @Test
    void no_dataSerie_uses_default() throws Exception {
        DataSerie defaultDataSerie = DataSerie.TIME_SERIES_DAILY_ADJUSTED;
        ArgumentCaptor<List<String>> symbols = ArgumentCaptor.forClass(List.class);

        StockDataDownloaderRunner downloader = new StockDataDownloaderRunner(mockAVDownloader);
        downloader.run("MSFT", "AAPL", "XYZ");

        verify(mockAVDownloader, times(1))
                .download(eq(defaultDataSerie), symbols.capture());

        assertIterableEquals(symbols.getValue(), Arrays.asList("MSFT", "AAPL", "XYZ"));

    }

    @Test
    void test_dataSerie_trows_exception() {

        StockDataDownloaderRunner downloader = new StockDataDownloaderRunner(mockAVDownloader);

        Exception exception = assertThrows(UnsupportedOperationException.class,
                () -> downloader.run("TEST_SERIE", "AAPL", "XYZ"));

        verify(mockAVDownloader, never())
                .download(any(), anyList());

        assertTrue(exception instanceof UnsupportedOperationException);
    }

}
