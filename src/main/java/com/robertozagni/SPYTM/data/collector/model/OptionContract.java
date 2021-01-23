package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

//@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OptionContract {

    @Id private String contractSymbol; //"SPY210104C00245000",

    private String underlyingSymbol; //"SPY",
    private LocalDate expiration; //UTC: Monday 4 January 2021 00:00:00
    @Enumerated(EnumType.STRING) private OptionType optionType;
    private Double strike; //":245.0,

    private String currency; //"USD",
    private String contractSize; //"REGULAR",

    List<OptionQuote> quotes;

    public static enum OptionType {
        CALL,
        PUT
    }
}
