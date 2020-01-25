package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieStockData;
import lombok.*;

import java.time.LocalDateTime;
import static com.robertozagni.SPYTM.data.collector.model.alphavantage.AVStockData.Constants.*;

@Data
@AllArgsConstructor
public class AVStockData {

    private LocalDateTime dateTime;
    @JsonProperty(value = OPEN_MARKER) private double open;
    @JsonProperty(value = HIGH_MARKER) private double high;
    @JsonProperty(value = LOW_MARKER) private double low;
    @JsonProperty(value = CLOSE_MARKER) private double close;
    @JsonProperty(value = ADJ_CLOSE_MARKER) private double adjustedClose;
    @JsonProperty(value = VOLUME_MARKER) private long volume;
    @JsonProperty(value = DIVIDEND_MARKER) private double dividendAmount;
    @JsonProperty(value = SPLIT_MARKER) private double splitCoefficient;

    public TimeSerieStockData toTimeSerieStockData() {
        return new TimeSerieStockData(
                dateTime, open, high, low, close, adjustedClose, volume, dividendAmount, splitCoefficient);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constants {
        static final String OPEN_MARKER = "1. open";
        static final String HIGH_MARKER = "2. high";
        static final String LOW_MARKER = "3. low";
        static final String CLOSE_MARKER = "4. close";
        static final String ADJ_CLOSE_MARKER = "5. adjusted close";
        static final String VOLUME_MARKER = "6. volume";
        static final String DIVIDEND_MARKER = "7. dividend amount";
        static final String SPLIT_MARKER = "8. split coefficient";
    }
}
