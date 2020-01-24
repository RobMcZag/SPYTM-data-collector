package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TimeSerieData {
    private static final String META_DATA_MARKER = "Meta Data";
    private static final String DAILY_DATA_MARKER = "Time Series (Daily)";
    private final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @JsonProperty(value = META_DATA_MARKER) private TimeSerieMetadata metadata;
    @JsonProperty(value = DAILY_DATA_MARKER) private Map<String, StockData> stockData;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    private static class TimeSerieMetadata {
        @JsonProperty(value = "1. Information") private String seriesInfo;
        @JsonProperty(value = "2. Symbol") private String symbol;
        @JsonProperty(value = "3. Last Refreshed") private String lastRefreshed;
        @JsonProperty(value = "4. Output Size") private String outputSize;
        @JsonProperty(value = "5. Time Zone") private String timeZone;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    private static class StockData {
        private static final String OPEN_MARKER = "1. open";
        private static final String HIGH_MARKER = "2. high";
        private static final String LOW_MARKER = "3. low";
        private static final String CLOSE_MARKER = "4. close";
        private static final String ADJ_CLOSE_MARKER = "5. adjusted close";
        private static final String VOLUME_MARKER = "6. volume";
        private static final String DIVIDEND_MARKER = "7. dividend amount";
        private static final String SPLIT_MARKER = "8. split coefficient";

        private LocalDateTime dateTime;
        @JsonProperty(value = OPEN_MARKER) private double open;
        @JsonProperty(value = HIGH_MARKER) private double high;
        @JsonProperty(value = LOW_MARKER) private double low;
        @JsonProperty(value = CLOSE_MARKER) private double close;
        @JsonProperty(value = ADJ_CLOSE_MARKER) private double adjustedClose;
        @JsonProperty(value = VOLUME_MARKER) private long volume;
        @JsonProperty(value = DIVIDEND_MARKER) private double dividendAmount;
        @JsonProperty(value = SPLIT_MARKER) private double splitCoefficient;

    }
}
