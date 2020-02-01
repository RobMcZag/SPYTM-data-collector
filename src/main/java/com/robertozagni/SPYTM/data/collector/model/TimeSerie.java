package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@AllArgsConstructor
public class TimeSerie {
    public static final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private TimeSerieMetadata metadata;
    /**
     * A map containing the DailyQuote for every desired date. Date is represented as an ISO string ("yyyy-MM-dd").
     */
    private Map<String, DailyQuote> data;


}
