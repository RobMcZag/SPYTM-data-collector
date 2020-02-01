package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity @IdClass(DailyQuote.DailyQuoteId.class)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DailyQuote {

    @Id @Enumerated(EnumType.STRING) private QuoteProvider provider;
    @Id @Enumerated(EnumType.STRING) private QuoteType quotetype;
    @Id @Column(length = 30) private String symbol;
    @Id private LocalDate date;     // @Temporal(TemporalType.DATE)

    @Embedded
    @Delegate
    private Quote quote;

    private double adjustedClose;
    private double dividendAmount;
    private double splitCoefficient;

    public DailyQuoteId getId() {
        return new DailyQuoteId(provider, quotetype, symbol, date);
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DailyQuoteId implements Serializable {
        private QuoteProvider provider;
        private QuoteType quotetype;
        private String symbol;
        private LocalDate date;
    }
}
