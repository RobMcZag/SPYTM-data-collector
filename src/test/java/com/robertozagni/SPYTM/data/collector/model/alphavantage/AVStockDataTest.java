package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.DataSerie;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AVStockDataTest {

    @Test
    void can_convert_to_TimeSerieStockData() {
        AVStockData sd = new AVStockData(
                DataSerie.TIME_SERIES_DAILY_ADJUSTED,
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
        DailyQuote tssd = sd.toTimeSerieStockData();

        assertEquals(tssd.getSerie(), DataSerie.TIME_SERIES_DAILY_ADJUSTED);
        assertEquals(tssd.getSymbol(), "SOME");
        assertEquals(tssd.getDate(), sd.getDate()); // TODO Test with multiple timezones / timestamp combinations that the date stays correct.
        assertEquals(tssd.getOpen(), sd.getOpen());
        assertEquals(tssd.getHigh(), sd.getHigh());
        assertEquals(tssd.getLow(), sd.getLow());
        assertEquals(tssd.getClose(), sd.getClose());
        assertEquals(tssd.getAdjustedClose(), sd.getAdjustedClose());
        assertEquals(tssd.getVolume(), sd.getVolume());
        assertEquals(tssd.getDividendAmount(), sd.getDividendAmount());
        assertEquals(tssd.getSplitCoefficient(), sd.getSplitCoefficient());
    }
}