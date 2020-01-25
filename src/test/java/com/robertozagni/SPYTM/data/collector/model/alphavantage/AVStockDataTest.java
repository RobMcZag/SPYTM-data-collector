package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.TimeSerieStockData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AVStockDataTest {

    @Test
    void can_convert_to_TimeSerieStockData() {
        AVStockData sd = new AVStockData(
                LocalDateTime.now(),
                123.4,
                129.99,
                120.01,
                122.2,
                122.5,
                123456,
                0.05,
                1.25
        );
        TimeSerieStockData tssd = sd.toTimeSerieStockData();

        assertEquals(tssd.getDateTimeUTC(), sd.getDateTime());
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