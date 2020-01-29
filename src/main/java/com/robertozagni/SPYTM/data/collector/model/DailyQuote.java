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

    @Id @Enumerated(EnumType.STRING) private DataSerie serie;
    @Id @Column(length = 30) private String symbol;
    @Id @Temporal(TemporalType.DATE) private LocalDate date;

    @Embedded
    @Delegate
    private Quote quote;

    private double adjustedClose;
    private double dividendAmount;
    private double splitCoefficient;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DailyQuoteId implements Serializable {

        private DataSerie serie;
        private String symbol;
        private LocalDate date;
    }
}
