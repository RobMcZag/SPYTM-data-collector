package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class YFTimeSerie {

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
        @JsonProperty private int regularMarketPrice;// 116.59,
        @JsonProperty private int chartPreviousClose;// 0.128,
        @JsonProperty private int priceHint;// 2,
        @JsonProperty private YFStockTradingPeriod currentTradingPeriod;// { ... },
        @JsonProperty private String dataGranularity;// "1d",
        @JsonProperty private String range;// "",
        @JsonProperty private List<String> validRanges;// ["1d","5d","1mo","3mo","6mo","1y","2y","5y","10y","ytd","max"]
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

}
