package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class YFv8TimeSerieTest {
    @Test
    void class_model_fits_json()  throws IOException {
        YFv8TimeSerie timeSerie = YahooStockDownloaderTestHelper.loadSampleAAPL_YFv8TimeSerie();

        assertNotNull(timeSerie);
        assertNotNull(timeSerie.getChart());
        assertNotNull(timeSerie.getChart().getResult());
        assertNotNull(timeSerie.getChart().getResult().get(0));
        assertNull(timeSerie.getChart().getError());

        YFv8TimeSerie.YFStockResultMeta stockResultMeta = timeSerie.getChart().getResult().get(0).getMeta();
        assertNotNull(stockResultMeta);
        assertNotNull(stockResultMeta.getValidRanges());
        assertNotNull(stockResultMeta.getCurrentTradingPeriod());
        assertEquals("USD", stockResultMeta.getCurrency()); // "currency": "USD"
        assertEquals("AAPL", stockResultMeta.getSymbol()); //"symbol": "AAPL",
        assertEquals("EQUITY", stockResultMeta.getInstrumentType()); // "instrumentType": "EQUITY",
        assertEquals(345479400, stockResultMeta.getFirstTradeDate()); // "firstTradeDate": 345479400,
        assertEquals(1606500002, stockResultMeta.getRegularMarketTime()); // "regularMarketTime": 1606500002,
        assertEquals(1606487400, stockResultMeta.getCurrentTradingPeriod().getPre().getEnd());
        assertEquals(1606487400, stockResultMeta.getCurrentTradingPeriod().getRegular().getStart());
        assertEquals(11, stockResultMeta.getValidRanges().size());

        List<Integer> timestamps = timeSerie.getChart().getResult().get(0).getTimestamp();
        assertNotNull(timestamps);
        assertEquals(345479400, timestamps.get(0)); // [345479400, ...]

        assertNotNull(timeSerie.getChart().getResult().get(0).getEvents());
        Map<Integer, YFv8TimeSerie.YFDividendEventData> dividendMap = timeSerie.getChart().getResult().get(0).getEvents().getDividends();
        assertNotNull(dividendMap);
        assertNotNull(dividendMap.get(1541687400));
        assertEquals(0.1825, dividendMap.get(1541687400).getAmount(), 0.001);
        assertEquals(1541687400, dividendMap.get(1541687400).getDate());
        Map<Integer, YFv8TimeSerie.YFSplitData> splitMap = timeSerie.getChart().getResult().get(0).getEvents().getSplits();
        assertNotNull(splitMap);
        assertNotNull(splitMap.get(1598880600));
        assertEquals(1598880600, splitMap.get(1598880600).getDate());
        assertEquals(4, splitMap.get(1598880600).getNumerator());
        assertEquals("4:1", splitMap.get(1598880600).getSplitRatio());

        assertNotNull(timeSerie.getChart().getResult().get(0).getIndicators());
        assertNotNull(timeSerie.getChart().getResult().get(0).getIndicators().getQuote());
        YFv8TimeSerie.YFStockQuotes yfStockQuotes = timeSerie.getChart().getResult().get(0).getIndicators().getQuote().get(0);
        assertNotNull(yfStockQuotes);
        assertEquals(timestamps.size(), yfStockQuotes.getOpen().size());
        assertEquals(timestamps.size(), yfStockQuotes.getClose().size());
        assertEquals(timestamps.size(), yfStockQuotes.getHigh().size());
        assertEquals(timestamps.size(), yfStockQuotes.getLow().size());
        assertEquals(timestamps.size(), yfStockQuotes.getVolume().size());
        // [ 0.1283482164144516, 0.1222098246216774, 0.11328125, 0.1155133917927742, ...]
        assertEquals(BigDecimal.valueOf(0.1283482164144516), yfStockQuotes.getOpen().get(0));
        assertEquals(new BigDecimal("0.1283482164144516"), yfStockQuotes.getOpen().get(0));
        assertEquals(BigDecimal.valueOf(0.1155133917927742), yfStockQuotes.getOpen().get(3));

        assertNotNull(timeSerie.getChart().getResult().get(0).getIndicators().getAdjclose());
        YFv8TimeSerie.YFStockAdjCloses yfStockAdjCloses = timeSerie.getChart().getResult().get(0).getIndicators().getAdjclose().get(0);
        assertNotNull(yfStockAdjCloses);
        assertNotNull(yfStockAdjCloses.getAdjclose());
        // [ 0.10026576370000839, 0.09503454715013504, 0.08805947750806808, 0.09023918211460114, ...]
        assertEquals(BigDecimal.valueOf(0.10026576370000839), yfStockAdjCloses.getAdjclose().get(0));
        assertEquals(BigDecimal.valueOf(0.08805947750806808), yfStockAdjCloses.getAdjclose().get(2));
    }
}
