package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerie.Constants.*;

@Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AVTimeSerie {

    @JsonProperty(value = META_DATA) private AVTimeSerieMetadata metadata;
    @JsonProperty(value = DAILY_SERIES) private Map<String, AVDailyQuote> avQuotes;
    @JsonProperty(value = WEEKLY_SERIES) private Map<String, AVDailyQuote> avQuotes_weekly;
    @JsonProperty(value = WEEKLY_ADJ_SERIES) private Map<String, AVDailyQuote> avQuotes_weeklyAdj;
    @JsonProperty(value = MONTHLY_SERIES) private Map<String, AVDailyQuote> avQuotes_monthly;
    @JsonProperty(value = MONTHLY_ADJ_SERIES) private Map<String, AVDailyQuote> avQuotes_monthlyAdj;

    public Map<String, AVDailyQuote> getAvQuotes() {
        if (avQuotes != null) {
            return avQuotes;
        } else if (avQuotes_weekly != null) {
            return avQuotes_weekly;
        } else if (avQuotes_weeklyAdj != null) {
            return avQuotes_weeklyAdj;
        } else if (avQuotes_monthly != null) {
            return avQuotes_monthly;
        } else if (avQuotes_monthlyAdj != null) {
            return avQuotes_monthlyAdj;
        }
        return new HashMap<>();
    }

    public TimeSerie toModel() {
        return toModel(this);
    }

    /**
     * Converts an Alpha Vantage specific data structure into the canonical SPYTM Model representation.
     * @param avTimeSerie a well formed AV Time Serie, i.e. with no nulls for data or metadata.
     * @return a Model time serie with the data from the AV Time Serie
     */
    static public TimeSerie toModel(AVTimeSerie avTimeSerie) {
        TimeSerieMetadata metaData = avTimeSerie.getMetadata().toTimeSerieMetadata();

        Map<String, AVDailyQuote> avQuoteMap = avTimeSerie.getAvQuotes();
        Map<String, DailyQuote> quotes = new HashMap<>();
        for (String tradingDate: avQuoteMap.keySet()) {
            AVDailyQuote avQuote = avQuoteMap.get(tradingDate);

            avQuote.setQuotetype(metaData.getQuotetype());
            avQuote.setSymbol(metaData.getSymbol());
            avQuote.setDate(parseTradingDateTime(tradingDate).toLocalDate());

            quotes.put(tradingDate, avQuote.toTimeSerieStockData());
        }
        return new TimeSerie(metaData, quotes);

    }

    private static LocalDateTime parseTradingDateTime(String tradingDateOrDateTime) {
        String tradingDateTime;
        if (tradingDateOrDateTime.contains(":")) {
            tradingDateTime = tradingDateOrDateTime;
        } else {
            tradingDateTime = tradingDateOrDateTime + AT_MIDNIGHT;
        }
        return LocalDateTime.parse(tradingDateTime, DATE_TIME_FORMAT);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constants {
        public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        public static final String AT_MIDNIGHT = " 00:00:00";


        public static final String META_DATA = "Meta Data";
        public static final String DAILY_SERIES  = "Time Series (Daily)";
        public static final String WEEKLY_SERIES = "Weekly Time Series";
        public static final String WEEKLY_ADJ_SERIES = "Weekly Adjusted Time Series";
        public static final String MONTHLY_SERIES = "Monthly Time Series";
        public static final String MONTHLY_ADJ_SERIES = "Monthly Adjusted Time Series";
    }
}
