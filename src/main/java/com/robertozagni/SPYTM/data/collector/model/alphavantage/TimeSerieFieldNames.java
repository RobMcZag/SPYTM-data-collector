package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)    // To avoid instantiation
public class TimeSerieFieldNames {
    public static final String META_DATA = "Meta Data";
    public static final String DAILY_SERIES ="Time Series (Daily)";
}
