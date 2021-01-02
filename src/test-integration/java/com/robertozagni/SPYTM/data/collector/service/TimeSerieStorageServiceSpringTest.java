package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloaderTestHelper;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import com.robertozagni.SPYTM.data.collector.repository.TimeSerieMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(args = {"TEST"} )
public class TimeSerieStorageServiceSpringTest {

    @MockBean DailyQuoteStorageService mockDailyQuoteStorageService;

    @Autowired TimeSerieMetadataRepository realMetadataRepository;

    private TimeSerieStorageService timeSerieStorageService;

    @BeforeEach
    void init() {
        timeSerieStorageService = new TimeSerieStorageService(realMetadataRepository, mockDailyQuoteStorageService);
    }

    @Test
    void service_actually_saves_metadata_and_forwards_quote_saving() throws IOException {
        TimeSerie msftTimeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_AVTimeSerie().toModel();

        when(mockDailyQuoteStorageService.saveAllQuotes(msftTimeSerie)).thenReturn(msftTimeSerie);

        timeSerieStorageService.save(msftTimeSerie);

        Optional<TimeSerieMetadata> retrievedTsMetadata = realMetadataRepository.findById(msftTimeSerie.getMetadata().getId());

        assertTrue(retrievedTsMetadata.isPresent());
        assertEquals(msftTimeSerie.getMetadata().getId(), retrievedTsMetadata.get().getId());
        assertEquals(msftTimeSerie.getMetadata(), retrievedTsMetadata.get());
    }

}
