package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

import static com.robertozagni.SPYTM.data.collector.model.alphavantage.TimeSerieFieldNames.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TimeSerieData {
//    private final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @JsonProperty(value = META_DATA) private TimeSerieMetadata metadata;
    @JsonProperty(value = DAILY_SERIES) private Map<String, StockData> stockData;

}
