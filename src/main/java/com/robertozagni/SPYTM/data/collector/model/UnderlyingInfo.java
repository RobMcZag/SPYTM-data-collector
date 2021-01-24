package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

//@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UnderlyingInfo {
    /** The undelying symbol this UnderlyingInfo is about. */
    @Id @Column(length = 30) private String symbol; //"SPY"

    private String shortName;//"SPDR S&P 500",
    private String longName;//"SPDR S&P 500 ETF Trust",
    private String quoteType; //"ETF",
    private String currency;//"USD",
    private String region; //"US",
    private String language; //"en-US",
    private String market;//"us_market",
    private Long sharesOutstanding;//917782016,

    private String quoteSourceName; //"Nasdaq Real Time Price",
//    private Long firstTradeDateMilliseconds;//728317800000, => GMT: Friday 29 January 1993 14:30:00
    private LocalDateTime firstTradeDateTime;//728317800000, => GMT: Friday 29 January 1993 14:30:00

    private String fullExchangeName;//"NYSEArca",
    private String exchange;//"PCX",
    private String marketState;//"CLOSED",
    private String exchangeTimezoneName;//"America/New_York",
    private String exchangeTimezoneShortName;//"EST",
    private Integer exchangeDataDelayedBy;//0,
    private Integer gmtOffSetMilliseconds;//-18000000, => 5 hours

//    private String financialCurrency;//"USD",
//    private Boolean tradeable;//false,
//    private Boolean triggerable;//true,
//    private Integer priceHint;//2,
//    private String messageBoardId;//"finmb_6160262",
//    private Boolean esgPopulated;//false,
//    private Integer sourceInterval;//15,

    private List<UnderlyingQuote> underlyingQuotes;
}
