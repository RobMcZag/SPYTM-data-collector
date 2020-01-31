package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AVTimeSerieTest {

    @Test
    void toModel() {
        AVTimeSerie avTimeSerie = makeAVTimeSerie();
        TimeSerie timeSerie = avTimeSerie.toModel();

        assertEquals(timeSerie.getMetadata().getSymbol(), avTimeSerie.getMetadata().getSymbol());
        assertEquals(timeSerie.getMetadata().getTimeZone(), avTimeSerie.getMetadata().getTimeZone());

        for (String tradeDate:timeSerie.getData().keySet()) {
            DailyQuote quote = timeSerie.getData().get(tradeDate);
            AVDailyQuote avQuote = avTimeSerie.getAvQuotes().get(tradeDate);

            assertEquals(quote.getProvider(), QuoteProvider.APLPHA_VANTAGE);
            assertEquals(quote.getQuotetype(), avQuote.getQuotetype());
            assertEquals(quote.getSymbol(), avQuote.getSymbol());
            assertEquals(quote.getDate(), avQuote.getDate());

            assertEquals(quote.getQuote().getOpen(), avQuote.getOpen());
            assertEquals(quote.getQuote().getClose(), avQuote.getClose());
            assertEquals(quote.getQuote().getHigh(), avQuote.getHigh());
            assertEquals(quote.getQuote().getLow(), avQuote.getLow());
            assertEquals(quote.getQuote().getVolume(), avQuote.getVolume());

            assertEquals(quote.getAdjustedClose(), avQuote.getAdjustedClose());
            assertEquals(quote.getDividendAmount(), avQuote.getDividendAmount());
            assertEquals(quote.getSplitCoefficient(), avQuote.getSplitCoefficient());
        }

    }

    @Test
    void nulls_in_AVTimeSerie_should_throw_NullPointerException_when_convert_toModel() {
        // null AVTimeSerie
        assertThrows(NullPointerException.class,
                () -> AVTimeSerie.toModel(null));

        // null AVTimeSerieMetadata
        assertThrows(NullPointerException.class,
                () -> {
                    AVTimeSerie avTimeSerie = makeAVTimeSerie();
                    avTimeSerie.setMetadata(null);
                    AVTimeSerie.toModel(avTimeSerie);
                });

        // null AVTime daily quotes
        assertThrows(NullPointerException.class,
                () -> {
                    AVTimeSerie avTimeSerie = makeAVTimeSerie();
                    avTimeSerie.setAvQuotes(null);
                    AVTimeSerie.toModel(avTimeSerie);
                });
    }

    /**
     * Creates a new AVTimeSerie object with some non null values.
     * @return a new AVTimeSerie
     */
    static AVTimeSerie makeAVTimeSerie() {
        AVTimeSerieMetadata metadata = AVTimeSerieMetadataTest.makeTestAVTImeSerieMetadata();

        Map<String, AVDailyQuote> quotes = new HashMap<>();
        AVDailyQuote dailyQuote = AVDailyQuoteTest.makeTestAVDailyQuote();
        quotes.put(dailyQuote.getDate().toString(), dailyQuote);

        return new AVTimeSerie(metadata,quotes);
    }

}