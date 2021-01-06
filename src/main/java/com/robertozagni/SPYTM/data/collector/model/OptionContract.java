package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.time.LocalDate;

//@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OptionContract {

    @Id private String contractSymbol; //"SPY210104C00245000",

    private String underlyingSymbol; //"SPY",
    private LocalDate expiration; //UTC: Monday 4 January 2021 00:00:00
    private OptionType optionType;
    private Double strike; //":245.0,

    private String currency; //"USD",
    private String contractSize; //"REGULAR",

    private Boolean inTheMoney; //true
    private Double impliedVolatility; // 2.49219126953125,
    private LocalDate lastTradeDate; //1608747716, in seconds: GMT: Wednesday 23 December 2020 18:21:56
    private Double lastPrice; // 124.45,
    private Double change; //0.0,
    private Double percentChange; //0.0,
    private Integer volume; //125,
    private Integer openInterest; //153,
    private Double bid; // 128.83,
    private Double ask; //129.35,

    public static enum OptionType {
        CALL,
        PUT
    }
}
