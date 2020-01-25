package com.robertozagni.SPYTM.data.collector.model;

import com.robertozagni.SPYTM.data.collector.model.alphavantage.AVTimeSerieMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@AllArgsConstructor
public class TimeSerie {
    public static final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private TimeSerieMetadata metadata;
    private Map<String, TimeSerieStockData> data;


}
