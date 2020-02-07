package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import com.robertozagni.SPYTM.data.collector.repository.TimeSerieMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TimeSerieStorageService {
    private final TimeSerieMetadataRepository metadataRepository;
    private final DailyQuoteStorageService dailyQuoteStorageService;

    @Autowired
    public TimeSerieStorageService(
            TimeSerieMetadataRepository metadataRepository, DailyQuoteStorageService dailyQuoteStorageService) {
        this.metadataRepository = metadataRepository;
        this.dailyQuoteStorageService = dailyQuoteStorageService;
    }

    /**
     * Saves the time serie by saving the metadata and the quotes.
     * @param timeSerie a time serie to be saved
     * @return the time serie with metadata and quotes as returned by the save process.
     */
    public TimeSerie save(TimeSerie timeSerie) {
        TimeSerieMetadata savedMetadata = metadataRepository.save(timeSerie.getMetadata());
        TimeSerie savedData = dailyQuoteStorageService.saveAllQuotes(timeSerie);

        return new TimeSerie(savedMetadata, savedData.getData());
    }
}
