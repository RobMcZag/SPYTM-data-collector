package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.DataSerie;
import com.robertozagni.SPYTM.data.collector.model.Quote;
import lombok.*;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static com.robertozagni.SPYTM.data.collector.model.alphavantage.AVStockData.Constants.*;

@Data
@AllArgsConstructor
public class AVStockData {

    private DataSerie serie;
    private String symbol;
    private LocalDate date;

    @JsonProperty(value = OPEN_MARKER) private double open;
    @JsonProperty(value = HIGH_MARKER) private double high;
    @JsonProperty(value = LOW_MARKER) private double low;
    @JsonProperty(value = CLOSE_MARKER) private double close;
    @JsonProperty(value = ADJ_CLOSE_MARKER) private double adjustedClose;
    @JsonProperty(value = VOLUME_MARKER) private long volume;
    @JsonProperty(value = DIVIDEND_MARKER) private double dividendAmount;
    @JsonProperty(value = SPLIT_MARKER) private double splitCoefficient;

    public DailyQuote toTimeSerieStockData() {
        return DailyQuote.builder()
                .serie(serie).symbol(symbol).date(date)  // Quote key
                .quote(Quote.builder().open(open).high(high).low(low).close(close).volume(volume).build())
                .adjustedClose(adjustedClose)
                .dividendAmount(dividendAmount)
                .splitCoefficient(splitCoefficient)
                .build();
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
