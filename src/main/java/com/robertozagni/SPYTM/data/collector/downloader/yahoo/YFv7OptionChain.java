package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.robertozagni.SPYTM.data.collector.model.OptionChain;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder @NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class YFv7OptionChain {

    @JsonProperty private YFv7OptionChain.YFOptionChain optionChain;

    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFOptionChain {
        @JsonProperty private List<YFv7OptionChain.YFOptionChainResult> result;
        @JsonProperty private String error;
    }

    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class YFOptionChainResult {
        @JsonProperty private String underlyingSymbol;
        @JsonProperty private List<Long> expirationDates;
        @JsonProperty private List<Double> strikes;
        @JsonProperty private Boolean hasMiniOptions;
        @JsonProperty private YFOptionUnderlying quote;
        @JsonProperty private List<YFOptionExpiration> options;
    }

    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFOptionExpiration {
        @JsonProperty private Long expirationDate; // 1609718400,
        @JsonProperty private Boolean hasMiniOptions;
        @JsonProperty private List<YFOptionQuote> calls;
        @JsonProperty private List<YFOptionQuote> puts;
    }

    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFOptionQuote {
        @JsonProperty private String contractSymbol; //"SPY210104C00245000",
        @JsonProperty private Double strike; //":245.0,
        @JsonProperty private String currency; //"USD",
        @JsonProperty private Double lastPrice; // 124.45,
        @JsonProperty private Double change; //0.0,
        @JsonProperty private Double percentChange; //0.0,
        @JsonProperty private Integer volume; //125,
        @JsonProperty private Integer openInterest; //153,
        @JsonProperty private Double bid; // 128.83,
        @JsonProperty private Double ask; //129.35,
        @JsonProperty private String contractSize; //"REGULAR",
        @JsonProperty private Long expiration; //1609718400,
        @JsonProperty private Long lastTradeDate; //1608747716,
        @JsonProperty private Double impliedVolatility; // 2.49219126953125, This is a %
        @JsonProperty private Boolean inTheMoney; //true
    }

    @Data @NoArgsConstructor @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class YFOptionUnderlying {
        @JsonProperty private String language; //"en-US",
        @JsonProperty private String region; //"US",
        @JsonProperty private String quoteType; //"ETF",
        @JsonProperty private String quoteSourceName; //"Nasdaq Real Time Price",
        @JsonProperty private Boolean triggerable;//true,
        @JsonProperty private String currency;//"USD",
        @JsonProperty private Long firstTradeDateMilliseconds;//728317800000, UTC: Fri Jan 29 1993 14:30:00 // EST: Fri Jan 29 1993 09:30:00
        @JsonProperty private Integer priceHint;//2,
        @JsonProperty private Double postMarketChangePercent;//0.0374121,
        @JsonProperty private Long postMarketTime;//1609462778,
        @JsonProperty private Double postMarketPrice;//374.39,
        @JsonProperty private Double postMarketChange;//0.140015,
        @JsonProperty private Double regularMarketChange;//1.89001,
        @JsonProperty private Double regularMarketChangePercent;//0.508082,
        @JsonProperty private Long regularMarketTime;//1609448402,
        @JsonProperty private Double regularMarketPrice;//373.88,
        @JsonProperty private Double regularMarketDayHigh;//374.65,
        @JsonProperty private String regularMarketDayRange;//"371.232 - 374.65",
        @JsonProperty private Double regularMarketDayLow;//371.232,
        @JsonProperty private Long regularMarketVolume;//55516031,
        @JsonProperty private Double regularMarketPreviousClose;//371.99,
        @JsonProperty private Double bid;//374.4,
        @JsonProperty private Double ask;//374.4,
        @JsonProperty private Integer bidSize;//9,
        @JsonProperty private Integer askSize;//11,
        @JsonProperty private String fullExchangeName;//"NYSEArca",
        @JsonProperty private String financialCurrency;//"USD",
        @JsonProperty private Double regularMarketOpen;//371.78,
        @JsonProperty private Long averageDailyVolume3Month;//69860170,
        @JsonProperty private Long averageDailyVolume10Day;//49412620,
        @JsonProperty private Double fiftyTwoWeekLowChange;//155.62001,
        @JsonProperty private Double fiftyTwoWeekLowChangePercent;//0.7130029,
        @JsonProperty private String fiftyTwoWeekRange;//"218.26 - 378.46",
        @JsonProperty private Double fiftyTwoWeekHighChange;//-4.5799866,
        @JsonProperty private Double fiftyTwoWeekHighChangePercent;//-0.01210164,
        @JsonProperty private Double fiftyTwoWeekLow;//218.26,
        @JsonProperty private Double fiftyTwoWeekHigh;//378.46,
        @JsonProperty private Double ytdReturn;//14.03,
        @JsonProperty private Double trailingThreeMonthReturns;//4.07,
        @JsonProperty private Double trailingThreeMonthNavReturns;//3.88,
        @JsonProperty private Long sharesOutstanding;//917782016,
        @JsonProperty private Double fiftyDayAverage;//366.16095,
        @JsonProperty private Double fiftyDayAverageChange;//7.719055,
        @JsonProperty private Double fiftyDayAverageChangePercent;//0.021081043,
        @JsonProperty private Double twoHundredDayAverage;//340.54218,
        @JsonProperty private Double twoHundredDayAverageChange;//33.33783,
        @JsonProperty private Double twoHundredDayAverageChangePercent;//0.09789633,
        @JsonProperty private Long marketCap;//343140335616,
        @JsonProperty private Integer sourceInterval;//15,
        @JsonProperty private Integer exchangeDataDelayedBy;//0,
        @JsonProperty private Boolean tradeable;//false,
        @JsonProperty private String marketState;//"CLOSED",
        @JsonProperty private String exchange;//"PCX",
        @JsonProperty private String shortName;//"SPDR S&P 500",
        @JsonProperty private String longName;//"SPDR S&P 500 ETF Trust",
        @JsonProperty private String messageBoardId;//"finmb_6160262",
        @JsonProperty private String exchangeTimezoneName;//"America/New_York",
        @JsonProperty private String exchangeTimezoneShortName;//"EST",
        @JsonProperty private Integer gmtOffSetMilliseconds;//-18 000 000,
        @JsonProperty private String market;//"us_market",
        @JsonProperty private Boolean esgPopulated;//false,
        @JsonProperty private String symbol;//"SPY"
    }

    public OptionChain toModel() {
        return toModel(this);
    }
    public OptionChain toModel(YFv7OptionChain yfOptionChain) {
        List<YFOptionChainResult> yfOptionChainResults = yfOptionChain.getOptionChain().getResult();
        assert yfOptionChainResults.size() == 1;
        YFOptionChainResult yfOptionChainResult = yfOptionChainResults.get(0);

        List<YFOptionExpiration> yfOptionExpirations = yfOptionChainResult.getOptions();
        assert yfOptionExpirations.size() == 1;
        YFOptionExpiration yfOptionExpiration = yfOptionExpirations.get(0);

        OptionChain optionChain = OptionChain.builder()
                .symbol(yfOptionChainResult.getUnderlyingSymbol())
                .expirationDate(dateOfEpochSecond(yfOptionExpiration.getExpirationDate()))
                .underlyingInfo(null)   // TODO
                .expirationDates(dateOfEpochSecond(yfOptionChainResult.getExpirationDates()))
                .strikes(null)          // TODO
                .optionQuotes(null)     // TODO
                .build();

        return optionChain;
    }


    public static List<LocalDate> dateOfEpochSecond(List<Long> list) {
        return list.stream().map(YFv7OptionChain::dateOfEpochSecond).collect(Collectors.toList());
    }
    public static LocalDate dateOfEpochSecond(Long seconds) {
        return dateOfEpochSecond(seconds, ZoneOffset.UTC);
    }
    public static LocalDate dateOfEpochSecond(Long seconds, ZoneId zone) {
        if (zone == null) { zone = ZoneOffset.UTC; }
        return Instant.ofEpochSecond(seconds).atZone(zone).toLocalDate();
    }
}
