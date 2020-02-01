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

@Data @AllArgsConstructor @NoArgsConstructor
public class AVTimeSerie {

    @JsonProperty(value = META_DATA) private AVTimeSerieMetadata metadata;
    @JsonProperty(value = DAILY_SERIES) private Map<String, AVDailyQuote> avQuotes;

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

        Map<String, DailyQuote> quotes = new HashMap<>();
        for (String tradingDate: avTimeSerie.getAvQuotes().keySet()) {
            AVDailyQuote avQuote = avTimeSerie.getAvQuotes().get(tradingDate);

            // TODO Replace with info from metaData
            avQuote.setQuotetype(QuoteType.DAILY_ADJUSTED);          // for now we always download this serie
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
        public static final String DAILY_SERIES ="Time Series (Daily)";
    }
}
