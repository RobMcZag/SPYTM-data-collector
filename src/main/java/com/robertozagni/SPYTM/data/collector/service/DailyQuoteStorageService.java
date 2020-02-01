package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.Quote;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import com.robertozagni.SPYTM.data.collector.repository.DailyQuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DailyQuoteStorageService {

    private final DailyQuoteRepository repository;

    @Autowired
    public DailyQuoteStorageService(DailyQuoteRepository repository) {
        this.repository = repository;
    }

    public TimeSerie saveAllQuotes(TimeSerie timeSerie) {
        HashMap<String, DailyQuote> savedData = new HashMap<>();
        for (DailyQuote quote: timeSerie.getData().values()) {
            savedData.put(quote.getDate().toString(), repository.save(quote));
        }
        return new TimeSerie(timeSerie.getMetadata(), savedData);
    }

}
