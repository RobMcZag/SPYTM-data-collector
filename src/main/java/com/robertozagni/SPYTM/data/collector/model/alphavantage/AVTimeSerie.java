package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.robertozagni.SPYTM.data.collector.model.alphavantage.AVTimeSerie.Constants.*;

@Data
@AllArgsConstructor
public class AVTimeSerie {

    @JsonProperty(value = META_DATA) private AVTimeSerieMetadata metadata;
    @JsonProperty(value = DAILY_SERIES) private Map<String, AVStockData> stockData;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constants {
        public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        public static final String AT_MIDNIGHT = " 00:00:00";


        public static final String META_DATA = "Meta Data";
        public static final String DAILY_SERIES ="Time Series (Daily)";
    }
}
