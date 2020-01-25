package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TimeSerieStockData {
    private LocalDateTime dateTimeUTC;
    private double open;
    private double high;
    private double low;
    private double close;
    private double adjustedClose;
    private long volume;
    private double dividendAmount;
    private double splitCoefficient;
}
