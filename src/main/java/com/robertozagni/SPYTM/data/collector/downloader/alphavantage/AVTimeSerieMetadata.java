package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerieMetadata.JsonPropertyName.*;
import static com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerieMetadata.SeriesInfoText.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class AVTimeSerieMetadata {

    @JsonProperty(value = SERIES_INFO) private String seriesInfo;
    @JsonProperty(value = SYMBOL) private String symbol;
    @JsonProperty(value = LAST_REFRESHED) private String lastRefreshed;
    @JsonProperty(value = OUTPUT_SIZE) private String outputSize;
    @JsonProperty(value = TIME_ZONE) private String timeZone;

    public TimeSerieMetadata toTimeSerieMetadata() {
        return TimeSerieMetadata.builder()
                .provider(QuoteProvider.ALPHA_VANTAGE)
                .quotetype(getQuoteType())
                .symbol(symbol)
                .description(seriesInfo)
                .timeZone(timeZone)
                .lastRefreshed(lastRefreshed)
                .build();
    }

    public   QuoteType getQuoteType() {
        return getQuoteType(this.seriesInfo);
    }

    private static QuoteType getQuoteType(String seriesInfo) {
        switch (seriesInfo) {
            case INTRADAY: return QuoteType.INTRADAY;
            case DAILY: return QuoteType.DAILY;
            case DAILY_ADJUSTED: return QuoteType.DAILY_ADJUSTED;
            case WEEKLY: return QuoteType.WEEKLY;
            case WEEKLY_ADJUSTED: return QuoteType.WEEKLY_ADJUSTED;
            case MONTHLY: return QuoteType.MONTHLY;
            case MONTHLY_ADJUSTED: return QuoteType.MONTHLY_ADJUSTED;

            default: return QuoteType.DAILY;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class JsonPropertyName {
        public static final String SERIES_INFO = "1. Information";
        public static final String SYMBOL = "2. Symbol";
        public static final String LAST_REFRESHED = "3. Last Refreshed";
        public static final String OUTPUT_SIZE = "4. Output Size";
        public static final String TIME_ZONE = "5. Time Zone";
    }

    public static class SeriesInfoText {
        public static final String INTRADAY = "Intraday (5min) open, high, low, close prices and volume";
        public static final String DAILY = "Daily Prices (open, high, low, close) and Volumes";
        public static final String DAILY_ADJUSTED = "Daily Time Series with Splits and Dividend Events";
        public static final String WEEKLY = "Weekly Prices (open, high, low, close) and Volumes";
        public static final String WEEKLY_ADJUSTED = "Weekly Adjusted Prices and Volumes";
        public static final String MONTHLY = "Monthly Prices (open, high, low, close) and Volumes";
        public static final String MONTHLY_ADJUSTED = "Monthly Adjusted Prices and Volumes";
        public static final String FX_INTRADAY = "FX Intraday (5min) Time Series";
        public static final String FX_DAILY = "Forex Daily Prices (open, high, low, close)";
        public static final String FX_WEEKLY = "Forex Weekly Prices (open, high, low, close)";
        public static final String FX_MONTHLY = "Forex Monthly Prices (open, high, low, close)";

    }
}
