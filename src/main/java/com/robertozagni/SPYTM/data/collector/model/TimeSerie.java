package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Time Series binds together some metadata, describing the serie, and some quotes for the serie.
 */
@Data @AllArgsConstructor
public class TimeSerie {

    /** The metadata for this TimeSerie */
    private TimeSerieMetadata metadata;

    /** A map containing the DailyQuote for every desired date. Date is represented as an ISO string ("yyyy-MM-dd"). */
    private Map<String, DailyQuote> data;

    /**
     * The id of this time serie, coming from its metadata.
     * @return the id for this timeserie.
     */
    private TimeSerieId getId() {
        return metadata.getId();
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TimeSerieId implements Serializable {
        private QuoteProvider provider;
        private QuoteType quotetype;
        private String symbol;
    }
}
