package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVDailyQuote;
import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AVDailyQuoteTest {

    @Test
    void can_convert_to_TimeSerieStockData() {
        AVDailyQuote sd = makeTestAVDailyQuote();
        DailyQuote tssd = sd.toTimeSerieStockData();

        assertEquals(tssd.getProvider(), QuoteProvider.APLPHA_VANTAGE);
        assertEquals(tssd.getQuotetype(), QuoteType.DAILY_ADJUSTED);
        assertEquals(tssd.getSymbol(), "SOME");
        assertEquals(tssd.getDate(), sd.getDate());
        assertEquals(tssd.getOpen(), sd.getOpen());
        assertEquals(tssd.getHigh(), sd.getHigh());
        assertEquals(tssd.getLow(), sd.getLow());
        assertEquals(tssd.getClose(), sd.getClose());
        assertEquals(tssd.getAdjustedClose(), sd.getAdjustedClose());
        assertEquals(tssd.getVolume(), sd.getVolume());
        assertEquals(tssd.getDividendAmount(), sd.getDividendAmount());
        assertEquals(tssd.getSplitCoefficient(), sd.getSplitCoefficient());
    }

    /**
     * Creates a new AVDailyQuote object with some non null values.
     * @return a new AVDailyQuote
     */
    static AVDailyQuote makeTestAVDailyQuote() {
        return new AVDailyQuote(
                QuoteType.DAILY_ADJUSTED,
                "SOME",
                LocalDate.now(),
                123.4,
                129.99,
                120.01,
                122.2,
                122.5,
                123456,
                0.05,
                1.25
        );
    }
}