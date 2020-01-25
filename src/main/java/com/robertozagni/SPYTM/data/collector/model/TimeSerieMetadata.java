package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeSerieMetadata {
    private String seriesInfo;
    private String symbol;
    private String lastRefreshed;
    private String outputSize;
    private String timeZone;
}
