package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Time Series binds together some metadata, describing the serie, and some quotes for te serie.
 */
@Data @AllArgsConstructor
public class TimeSerie {
    public static final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private TimeSerieId id;

    /** The metadata for this TimeSerie */
    private TimeSerieMetadata metadata;

    /**
     * A map containing the DailyQuote for every desired date. Date is represented as an ISO string ("yyyy-MM-dd").
     */
    private Map<String, DailyQuote> data;

    public TimeSerie(TimeSerieMetadata metadata, Map<String, DailyQuote> data) {
        this.id = new TimeSerieId(metadata.getProvider(), metadata.getQuotetype(), metadata.getSymbol());
        this.metadata = metadata;
        this.data = data;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TimeSerieId implements Serializable {
        private QuoteProvider provider;
        private QuoteType quotetype;
        private String symbol;
    }
}
