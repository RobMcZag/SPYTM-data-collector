package com.robertozagni.SPYTM.data.collector.repository;

import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyQuoteRepository extends CrudRepository<DailyQuote, DailyQuote.DailyQuoteId> {
    List<DailyQuote> findBySymbolAndQuotetypeAndProvider(String symbol, QuoteType type, QuoteProvider provider);
}
