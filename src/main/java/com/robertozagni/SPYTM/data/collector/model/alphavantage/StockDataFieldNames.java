package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)    // To avoid instantiation
public class StockDataFieldNames {
    static final String OPEN_MARKER = "1. open";
    static final String HIGH_MARKER = "2. high";
    static final String LOW_MARKER = "3. low";
    static final String CLOSE_MARKER = "4. close";
    static final String ADJ_CLOSE_MARKER = "5. adjusted close";
    static final String VOLUME_MARKER = "6. volume";
    static final String DIVIDEND_MARKER = "7. dividend amount";
    static final String SPLIT_MARKER = "8. split coefficient";
}
