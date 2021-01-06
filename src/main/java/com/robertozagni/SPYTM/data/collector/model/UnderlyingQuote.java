package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

//@Entity @IdClass(UnderlyingQuote.UnderlyingQuoteId.class)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UnderlyingQuote {

    @Id @Column(length = 30) private String symbol;
    @Id private LocalDate date;

    public UnderlyingQuoteId getId() { return new UnderlyingQuoteId(symbol, date); }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UnderlyingQuoteId implements Serializable {
        private String symbol;
        private LocalDate date;
    }

    private Double bid;//374.4,
    private Double ask;//374.4,
    private Integer bidSize;//9,
    private Integer askSize;//11,

    private Long marketCap;//343140335616,

    private Double regularMarketChange;//1.89001,
    private Double regularMarketChangePercent;//0.508082,
    private Integer regularMarketTime;//1609448402,
    private Double regularMarketPrice;//373.88,
    private Double regularMarketDayHigh;//374.65,
    private String regularMarketDayRange;//"371.232 - 374.65",
    private Double regularMarketDayLow;//371.232,
    private Long regularMarketVolume;//55516031,
    private Double regularMarketPreviousClose;//371.99,
    private Double regularMarketOpen;//371.78,

    private Double postMarketChangePercent;//0.0374121,
    private Integer postMarketTime;//1609462778,
    private Double postMarketPrice;//374.39,
    private Double postMarketChange;//0.140015,

    private Long averageDailyVolume3Month;//69860170,
    private Long averageDailyVolume10Day;//49412620,
    private Double fiftyTwoWeekLowChange;//155.62001,
    private Double fiftyTwoWeekLowChangePercent;//0.7130029,
    private String fiftyTwoWeekRange;//"218.26 - 378.46",
    private Double fiftyTwoWeekHighChange;//-4.5799866,
    private Double fiftyTwoWeekHighChangePercent;//-0.01210164,
    private Double fiftyTwoWeekLow;//218.26,
    private Double fiftyTwoWeekHigh;//378.46,
    private Double ytdReturn;//14.03,
    private Double trailingThreeMonthReturns;//4.07,
    private Double trailingThreeMonthNavReturns;//3.88,
    private Double fiftyDayAverage;//366.16095,
    private Double fiftyDayAverageChange;//7.719055,
    private Double fiftyDayAverageChangePercent;//0.021081043,
    private Double twoHundredDayAverage;//340.54218,
    private Double twoHundredDayAverageChange;//33.33783,
    private Double twoHundredDayAverageChangePercent;//0.09789633,

}
