package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data @AllArgsConstructor @Builder
public class OptionChain {
    /** The undelying symbol this option chain is about. */
    private String symbol;

    /** The expiration date this option chain is about. */
    private LocalDate expirationDate;

    /**
     * The id of this option chain.
     * @return the id for this option chain.
     */
    private OptionChainId getId() { return new OptionChainId(symbol,expirationDate); }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OptionChainId implements Serializable {
        private String symbol;
        private LocalDate expirationDate;
    }

    /** the "static values", not date based
     *  containing a list of underlying quote that contain the values chainging daily
     */
    private UnderlyingInfo underlyingInfo;

    private List<LocalDate> expirationDates;
    private List<Double> strikes;

    private List<OptionContract> optionQuotes;

}
