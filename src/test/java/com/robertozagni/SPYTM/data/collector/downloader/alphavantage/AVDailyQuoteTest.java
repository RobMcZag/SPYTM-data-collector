package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AVDailyQuoteTest {

    public static final String TEST_SYMBOL_NAME = "TEST_SYMBOL";
    public static final int TEST_VOLUME = 123456;

    @Test
    void can_convert_to_TimeSerieStockData() {
        AVDailyQuote sd = makeTestAVDailyQuote();
        DailyQuote tssd = sd.toTimeSerieStockData();

        assertEquals(tssd.getProvider(), QuoteProvider.ALPHA_VANTAGE);
        assertEquals(tssd.getQuotetype(), QuoteType.DAILY_ADJUSTED);
        assertEquals(tssd.getSymbol(), TEST_SYMBOL_NAME);
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

    @Test
    void all_volumes_are_set() {
        AVDailyQuote avQuote = makeTestAVDailyQuote();
        Assertions.assertEquals(TEST_VOLUME, avQuote.getVolume());
        Assertions.assertEquals(TEST_VOLUME, avQuote.getVolume_daily());
    }

    /**
     * Creates a new AVDailyQuote object with some non null values.
     * @return a new AVDailyQuote
     */
    static AVDailyQuote makeTestAVDailyQuote() {
        return AVDailyQuote.builder()
                .quotetype(QuoteType.DAILY_ADJUSTED)
                .symbol(TEST_SYMBOL_NAME)
                .date(LocalDate.now())
                .open(123.4)
                .high(129.99)
                .low(120.01)
                .close(122.2)
                .adjustedClose(122.5)
                .volume(TEST_VOLUME)
                .dividendAmount(0.05)
                .splitCoefficient(1.25)
                .build();
    }
}