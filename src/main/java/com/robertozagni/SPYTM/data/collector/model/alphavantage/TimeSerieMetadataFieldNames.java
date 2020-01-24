package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)    // To avoid instantiation
public class TimeSerieMetadataFieldNames {
    public static final String SERIES_INFO = "1. Information";
    public static final String SYMBOL = "2. Symbol";
    public static final String LAST_REFRESHED = "3. Last Refreshed";
    public static final String OUTPUT_SIZE = "4. Output Size";
    public static final String TIME_ZONE = "5. Time Zone";
}
