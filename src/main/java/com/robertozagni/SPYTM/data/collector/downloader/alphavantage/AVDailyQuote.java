package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.Quote;
import lombok.*;

import java.time.LocalDate;

import static com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVDailyQuote.Constants.*;

/**
 * Data structure for receiving quotes with full day pricing, including eventual adjusting info,
 * received trough the wire in JSON format by Alpha Vantage APIs.
 *
 * This class has extra fields to be able to enrich the object and convert it to the Model representation.
 */
@Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AVDailyQuote {

    /* Extra fields to be able to enrich the object and convert it to the Model representation */
    private QuoteType quotetype;
    private String symbol;
    private LocalDate date;

    /* Fields containing the data sent trough the wire in JSON format. */
    @JsonProperty(value = OPEN_MARKER) private double open;
    @JsonProperty(value = HIGH_MARKER) private double high;
    @JsonProperty(value = LOW_MARKER) private double low;
    @JsonProperty(value = CLOSE_MARKER) private double close;
    @JsonProperty(value = ADJ_CLOSE_MARKER) private double adjustedClose;
    @JsonProperty(value = DAILY_VOLUME_MARKER) private long volume_daily;
    @JsonProperty(value = ADJ_VOLUME_MARKER) private long volume;
    @JsonProperty(value = DIVIDEND_MARKER) private double dividendAmount;
    @JsonProperty(value = SPLIT_MARKER) private double splitCoefficient;

    public DailyQuote toTimeSerieStockData() {
        return DailyQuote.builder()
                .provider(QuoteProvider.APLPHA_VANTAGE)
                .quotetype(quotetype).symbol(symbol).date(date)  // Quote key
                .quote(Quote.builder().open(open).high(high).low(low).close(close).volume(getVolume()).build())
                .adjustedClose(adjustedClose)
                .dividendAmount(dividendAmount)
                .splitCoefficient(splitCoefficient)
                .build();
    }

    public long getVolume() {
        return volume != 0 ? volume : volume_daily;
    }
    public long getVolume_daily() {
        return getVolume();
    }

    public void setVolume(long volume) {
        this.volume = volume;
        this.volume_daily = volume;
    }
    public void setVolume_daily(long volume_daily) {
        setVolume(volume_daily);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constants {
        static final String OPEN_MARKER = "1. open";
        static final String HIGH_MARKER = "2. high";
        static final String LOW_MARKER = "3. low";
        static final String CLOSE_MARKER = "4. close";
        static final String ADJ_CLOSE_MARKER = "5. adjusted close";
        static final String DAILY_VOLUME_MARKER = "5. volume";
        static final String ADJ_VOLUME_MARKER = "6. volume";
        static final String DIVIDEND_MARKER = "7. dividend amount";
        static final String SPLIT_MARKER = "8. split coefficient";
    }
}
