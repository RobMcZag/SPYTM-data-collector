package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.robertozagni.SPYTM.data.collector.model.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class YFv8TimeSerie {

    @JsonProperty private YFChart chart;

    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFChart {
        @JsonProperty private List<YFStockResult> result;
        @JsonProperty private String error;
    }

    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class YFStockResult {
        @JsonProperty private YFStockResultMeta meta;
        @JsonProperty private List<Integer> timestamp;
        @JsonProperty private YFEvents events;
        @JsonProperty private YFIndicators indicators;

    }
    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFStockResultMeta {
        @JsonProperty private String currency; // "USD",
        @JsonProperty private String symbol;// "AAPL",
        @JsonProperty private String exchangeName;// "NMS",
        @JsonProperty private String instrumentType;// "EQUITY",
        @JsonProperty private int firstTradeDate;// 345479400,
        @JsonProperty private int regularMarketTime;// 1606500002,
        @JsonProperty private int gmtoffset;// -18000,
        @JsonProperty private String timezone;// "EST",
        @JsonProperty private String exchangeTimezoneName;// "America/New_York",
        @JsonProperty private double regularMarketPrice;// 116.59,
        @JsonProperty private double chartPreviousClose;// 0.128,
        @JsonProperty private int priceHint;// 2,
        @JsonProperty private YFStockTradingPeriod currentTradingPeriod;// { ... },
        @JsonProperty private String dataGranularity;// "1d",
        @JsonProperty private String range;// "",
        @JsonProperty private List<String> validRanges;// ["1d","5d","1mo","3mo","6mo","1y","2y","5y","10y","ytd","max"]

        public TimeSerieMetadata toTimeSerieMetadata() {
            return TimeSerieMetadata.builder()
                    .provider(QuoteProvider.YAHOO_FINANCE)
                    .quotetype(getQuoteType(dataGranularity))
                    .symbol(symbol)
                    .description(dataGranularity)
                    .timeZone(timezone)
                    .lastRefreshed(Instant.ofEpochSecond(regularMarketTime).toString())
                    .build();

        }

        /**
         * Returns the mapping between Yahoo Finance granularity values and our Quote Types.
         * @param dataGranularity the granularity of the timeserie from Yahoo
         * @throws IllegalArgumentException if the granularity does not map to a Quote Type
         * @return the QuoteType corresponding to the given granularity
         */
        private QuoteType getQuoteType(String dataGranularity) {
            switch (dataGranularity) {
                case "1d": return QuoteType.DAILY_ADJUSTED;
                case "5d": return QuoteType.WEEKLY_ADJUSTED;
                case "1mo": return QuoteType.MONTHLY_ADJUSTED;
                default: throw new IllegalArgumentException();  // "3mo","6mo","1y","2y","5y","10y","ytd","max"
            }
        }
    }
    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFStockTradingPeriod {
        @JsonProperty private YFTradingPeriod pre; // {"timezone": "EST","start": 1606467600,"end": 1606487400,"gmtoffset": -18000},
        @JsonProperty private YFTradingPeriod regular; // { "timezone": "EST", "start": 1606487400, "end": 1606500000, "gmtoffset": -18000 },
        @JsonProperty private YFTradingPeriod post;// { "timezone": "EST", "start": 1606500000, "end": 1606525200, "gmtoffset": -18000 }
    }
    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFTradingPeriod {
        @JsonProperty private String timezone;// "EST",
        @JsonProperty private int start;// 1606467600,
        @JsonProperty private int end;// 1606487400,
        @JsonProperty private int gmtoffset; // -18000
    }

    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class YFEvents {
        @JsonProperty private Map<Integer, YFDividendEventData> dividends; // { "753719400": { ...},
        @JsonProperty private Map<Integer, YFSplitData> splits; // "961594200": // { ...}
    }
    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFDividendEventData {
        @JsonProperty private double amount; //": 0.00107,
        @JsonProperty private int date; //": 753719400
    }
    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFSplitData {
        @JsonProperty private int date; // "date": 961594200,
        @JsonProperty private int numerator; // 2,
        @JsonProperty private int denominator; // 1,
        @JsonProperty private String splitRatio; // "2:1"
    }

    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class YFIndicators {
        @JsonProperty private List<YFStockQuotes> quote;
        @JsonProperty private List<YFStockAdjCloses> adjclose;
    }
    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class YFStockQuotes {
        @JsonProperty private List<BigDecimal> open; // [...]
        @JsonProperty private List<BigDecimal> close; // [...]
        @JsonProperty private List<BigDecimal> low; // [...]
        @JsonProperty private List<BigDecimal> high; // [...]
        @JsonProperty private List<Long> volume; // [...]
    }
    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class YFStockAdjCloses {
        @JsonProperty private List<BigDecimal> adjclose; // [...]
    }

    /**
     * Converts an Yahoo Finance specific data structure into the canonical SPYTM Model representation.
     * @param yfTimeSerie a well formed YF Time Serie, i.e. with no nulls for data or metadata.
     * @return a Model time serie with the data from the YF Time Serie
     */
    static public TimeSerie toModel(YFv8TimeSerie yfTimeSerie) {
        YFStockResult stockResult = yfTimeSerie.getChart().getResult().get(0);
        TimeSerieMetadata metaData = stockResult.getMeta().toTimeSerieMetadata();

        List<Integer> timestampList = stockResult.getTimestamp();
        YFStockQuotes quotes = stockResult.getIndicators().getQuote().get(0);
        YFStockAdjCloses adjCloses = stockResult.getIndicators().getAdjclose().get(0);

        YFEvents stockResultEvents = getEvents(stockResult);
        Map<Integer, YFDividendEventData> dividendsMap = stockResultEvents.getDividends();
        Map<Integer, YFSplitData> splitsMap = stockResultEvents.getSplits();

        Map<String, DailyQuote> quoteMap = new HashMap<>();
        for (int i=0; i < timestampList.size(); i++) {
            int timestamp = timestampList.get(i);
            LocalDate quoteDate = getDate(timestamp);

            try {
                DailyQuote dailyQuote = DailyQuote.builder()
                            .provider(QuoteProvider.YAHOO_FINANCE)      // Quote key
                            .quotetype(metaData.getQuotetype())         // Quote key
                            .symbol(metaData.getSymbol())               // Quote key
                            .date(quoteDate)                            // Quote key
                            .quote(
                                    Quote.builder()
                                            .open(getDouble(quotes.getOpen(), i))
                                            .high(getDouble(quotes.getHigh(), i))
                                            .low(getDouble(quotes.getLow(), i))
                                            .close(getDouble(quotes.getClose(), i))
                                            .volume(getVolume(quotes, i))
                                            .build()
                            )
                            .adjustedClose(getDouble(adjCloses.getAdjclose(), i))
                            .dividendAmount(getDividend(dividendsMap, timestamp))
                            .splitCoefficient(getSplitCoefficient(splitsMap, timestamp))
                            .build();

                quoteMap.put(quoteDate.format(DateTimeFormatter.ISO_LOCAL_DATE), dailyQuote);

            } catch (NoQuoteException continue_without_quote) { /* no quote for this timestamp */}

        }
        return new TimeSerie(metaData, quoteMap);

    }

    /**
     * Extracts and returns the events (spits and dividends) accounting for no data of any type.
     * @param stockResult The results received by Yahoo
     * @return The Events received or the empty containers for missing data.
     */
    private static YFEvents getEvents(YFStockResult stockResult) {
        Map<Integer, YFDividendEventData> emptyDividendMap = new HashMap<>(); //events.getDividends();
        Map<Integer, YFSplitData> emptySplitMap = new HashMap<>();   // events.getSplits();

        YFEvents events = stockResult.getEvents();
        if (events == null) {
            events = new YFEvents();
            events.setDividends(emptyDividendMap);
            events.setSplits(emptySplitMap);
        } else {
            if (events.getDividends() == null) {
                events.setDividends(emptyDividendMap);
            }
            if (events.getSplits() == null) {
                events.setSplits(emptySplitMap);
            }
        }
        return events;
    }

    private static Long getVolume(YFStockQuotes quotes, int i) throws NoQuoteException {
        Long volume = quotes.getVolume().get(i);
        if (volume == null) {
            throw new NoQuoteException();
        }
        return volume;
    }

    private static double getDouble(List<BigDecimal> list, int i) throws NoQuoteException {
        BigDecimal bigDecimal = list.get(i);
        if (bigDecimal == null) {
            throw new NoQuoteException();
        }
        return bigDecimal.doubleValue();
    }

    private static double getSplitCoefficient(Map<Integer, YFSplitData> splitsMap, int timestamp) {
        YFSplitData splitData = splitsMap.get(timestamp);
        return splitData != null ? ((double) splitData.getNumerator()) / splitData.getDenominator() : 1;
    }

    private static double getDividend(Map<Integer, YFDividendEventData> dividendsMap, int timestamp) {
        YFDividendEventData dividendEventData = dividendsMap.get(timestamp);
        return dividendEventData != null ? dividendEventData.getAmount() : 0;
    }

    private static LocalDate getDate(int timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC).toLocalDate();
    }
}
