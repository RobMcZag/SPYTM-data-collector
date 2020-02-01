package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.repository.DailyQuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DailyQuoteStorageService {

    private final DailyQuoteRepository repository;

    @Autowired
    public DailyQuoteStorageService(DailyQuoteRepository repository) {
        this.repository = repository;
    }

    public TimeSerie saveAllQuotes(TimeSerie timeSerie) {
        Iterable<DailyQuote> savedQuotes = saveAll(timeSerie.getData().values());

        HashMap<String, DailyQuote> savedData = new HashMap<>();
        for (DailyQuote quote: savedQuotes) {
            savedData.put(quote.getDate().toString(), quote);
        }
        return new TimeSerie(timeSerie.getMetadata(), savedData);
    }

    public Iterable<DailyQuote> saveAll(Iterable<DailyQuote> quotes) {
        return repository.saveAll(quotes);
    }

}
