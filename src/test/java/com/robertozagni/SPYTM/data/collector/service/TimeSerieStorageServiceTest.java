package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.downloader.StockDataDownloaderRunner;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloaderTestHelper;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import com.robertozagni.SPYTM.data.collector.repository.TimeSerieMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimeSerieStorageServiceTest {

    @Mock TimeSerieMetadataRepository mockMetadataRepository;
    @Mock DailyQuoteStorageService mockDailyQuoteStorageService;

    private TimeSerieStorageService timeSerieStorageService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        timeSerieStorageService = new TimeSerieStorageService(mockMetadataRepository, mockDailyQuoteStorageService);
    }

    @Test
    void can_save_metadata_and_data() throws IOException {
        TimeSerie msftTimeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_AVTimeSerie().toModel();

        when(mockMetadataRepository.save(msftTimeSerie.getMetadata()))
                .thenReturn(msftTimeSerie.getMetadata());
        when(mockDailyQuoteStorageService.saveAllQuotes(msftTimeSerie))
                .thenReturn(msftTimeSerie);

        TimeSerie savedTimeSerie = timeSerieStorageService.save(msftTimeSerie);

        verify(mockMetadataRepository, times(1))
                .save(any(TimeSerieMetadata.class));
        verify(mockDailyQuoteStorageService, times(1))
                .saveAllQuotes(any(TimeSerie.class));

        assertEquals(msftTimeSerie, savedTimeSerie);

    }
}