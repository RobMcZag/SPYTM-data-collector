package com.robertozagni.SPYTM.data.collector.repository;

import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyQuoteRepository extends CrudRepository<DailyQuote, DailyQuote.DailyQuoteId> {
    List<DailyQuote> findBySymbolAndQuotetypeAndProvider(String symbol, QuoteType type, QuoteProvider provider);

    @Query(value =
            "SELECT * FROM spytm.daily_quote\n" +
            "WHERE (provider, quotetype, symbol, date) IN (\n" +
            "    SELECT provider, quotetype, symbol, max(date)\n" +
            "    FROM spytm.daily_quote\n" +
            "    GROUP BY provider, quotetype, symbol\n" +
            "    )"
            , nativeQuery = true)
    List<DailyQuote> getLatestQuotes();

    @Query(value =
            "SELECT * FROM spytm.daily_quote\n" +
                    "WHERE (provider, quotetype, symbol, date) IN (\n" +
                    "    SELECT provider, quotetype, symbol, max(date)\n" +
                    "    FROM spytm.daily_quote\n" +
                    "    GROUP BY provider, quotetype, symbol \n" +
                    "    WHERE symbol = :symbol \n" +
                    "    )"
            , nativeQuery = true)
    List<DailyQuote> getLatestQuoteBySymbol( @Param("symbol") String symbol );

    @Query(value =
            "SELECT * FROM spytm.daily_quote\n" +
                    "WHERE (provider, quotetype, symbol, date) IN (\n" +
                    "    SELECT provider, quotetype, symbol, max(date)\n" +
                    "    FROM spytm.daily_quote\n" +
                    "    GROUP BY provider, quotetype, symbol \n" +
                    "    WHERE provider = :provider and quotetype = :type \n" +
                    "    )"
            , nativeQuery = true)
    List<DailyQuote> getLatestQuoteByQuotetypeAndProvider(
            @Param("type") QuoteType type,
            @Param("provider") QuoteProvider provider);

}
