package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import static com.robertozagni.SPYTM.data.collector.model.alphavantage.StockDataFieldNames.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockData {

    private LocalDateTime dateTime;
    @JsonProperty(value = OPEN_MARKER) private double open;
    @JsonProperty(value = HIGH_MARKER) private double high;
    @JsonProperty(value = LOW_MARKER) private double low;
    @JsonProperty(value = CLOSE_MARKER) private double close;
    @JsonProperty(value = ADJ_CLOSE_MARKER) private double adjustedClose;
    @JsonProperty(value = VOLUME_MARKER) private long volume;
    @JsonProperty(value = DIVIDEND_MARKER) private double dividendAmount;
    @JsonProperty(value = SPLIT_MARKER) private double splitCoefficient;

}
