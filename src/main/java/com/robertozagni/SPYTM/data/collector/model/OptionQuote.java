package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OptionQuote {

    @Id @Column(length = 30) private String contractSymbol;
    @Id private LocalDate quoteDate;     // @Temporal(TemporalType.DATE)

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OptionQuoteId implements Serializable {
        private String contractSymbol;
        private LocalDate quoteDate;
    }
    public OptionQuote.OptionQuoteId getId() {
        return new OptionQuote.OptionQuoteId(contractSymbol, quoteDate);
    }

    private Boolean inTheMoney; //true
    private Double impliedVolatility; // 2.49219126953125,
    private LocalDateTime lastTradeDate; //1608747716, in seconds: GMT: Wednesday 23 December 2020 18:21:56
    private Double lastPrice; // 124.45,
    private Double change; //0.0,
    private Double percentChange; //0.0,
    private Integer volume; //125,
    private Integer openInterest; //153,
    private Double bid; // 128.83,
    private Double ask; //129.35,

}
