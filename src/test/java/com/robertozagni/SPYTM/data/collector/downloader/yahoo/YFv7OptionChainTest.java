package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.robertozagni.SPYTM.data.collector.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class YFv7OptionChainTest {

    @Test
    void class_model_fits_json() throws IOException {
        YFv7OptionChain optionChain = YahooStockDownloaderTestHelper.loadSampleSPY_YFv7OptionChain();

        assertNotNull(optionChain);
        assertNotNull(optionChain.getOptionChain());
        assertNull(optionChain.getOptionChain().getError());
        assertNotNull(optionChain.getOptionChain().getResult());
        assertEquals(1, optionChain.getOptionChain().getResult().size());

        YFv7OptionChain.YFOptionChainResult optionChainResult = optionChain.getOptionChain().getResult().get(0);
        assertEquals("SPY",optionChainResult.getUnderlyingSymbol());
        assertEquals(LocalDate.parse("2021-01-04"),
                Instant.ofEpochSecond(optionChainResult.getExpirationDates().get(0)).atZone(ZoneOffset.UTC).toLocalDate() );
        assertEquals(245.0,optionChainResult.getStrikes().get(0) );
        assertFalse(optionChainResult.getHasMiniOptions());

        YFv7OptionChain.YFOptionUnderlying optionUnderlying = optionChainResult.getQuote();
        assertNotNull(optionUnderlying);
        assertEquals("en-US", optionUnderlying.getLanguage() );
        assertEquals("ETF", optionUnderlying.getQuoteType() );
        assertEquals(374.4, optionUnderlying.getBid() );
        assertEquals(14.03, optionUnderlying.getYtdReturn() );
        assertEquals(343140335616L, optionUnderlying.getMarketCap() );
        assertEquals("SPDR S&P 500", optionUnderlying.getShortName() );
        assertEquals(-18000000, optionUnderlying.getGmtOffSetMilliseconds() );
        assertFalse(optionUnderlying.getEsgPopulated() );
        assertEquals("SPY", optionUnderlying.getSymbol() );

        YFv7OptionChain.YFOptionExpiration optionExpiration = optionChainResult.getOptions().get(0);
        assertNotNull(optionExpiration);
        assertEquals(1609718400, optionExpiration.getExpirationDate());
        assertFalse(optionExpiration.getHasMiniOptions());
        assertNotNull(optionExpiration.getCalls());
        assertNotNull(optionExpiration.getPuts());

        YFv7OptionChain.YFOptionQuote firstCallQuote = optionExpiration.getCalls().get(0);
        assertEquals("SPY210104C00245000", firstCallQuote.getContractSymbol());
        assertEquals(245.0, firstCallQuote.getStrike() );
        assertEquals("USD", firstCallQuote.getCurrency() );
        assertEquals(124.45, firstCallQuote.getLastPrice() );
        assertEquals(0.0, firstCallQuote.getChange() );
        assertEquals(0.0, firstCallQuote.getPercentChange() );
        assertEquals(125, firstCallQuote.getVolume() );
        assertEquals(153, firstCallQuote.getOpenInterest() );
        assertEquals(128.83, firstCallQuote.getBid() );
        assertEquals(129.35, firstCallQuote.getAsk() );
        assertEquals("REGULAR", firstCallQuote.getContractSize() );
        assertEquals(1609718400, firstCallQuote.getExpiration() );
        assertEquals(1608747716, firstCallQuote.getLastTradeDate() );
        assertEquals(2.49219126953125, firstCallQuote.getImpliedVolatility(), 0.0001 );
        assertTrue( firstCallQuote.getInTheMoney() );
    }

    @Test
    void to_model_generate_correct_objects() throws IOException {
        YFv7OptionChain yfOptionChain = YahooStockDownloaderTestHelper.loadSampleSPY_YFv7OptionChain();
        YFv7OptionChain.YFOptionChainResult yfOptionChainResult = yfOptionChain.getOptionChain().getResult().get(0);

        OptionChain optionChain = yfOptionChain.toModel();
        assertNotNull(optionChain);
        assertEquals("SPY", optionChain.getUnderlyingSymbol());
        assertEquals(LocalDate.parse("2021-01-04"), optionChain.getExpirationDate());
        assertIterableEquals(expectedExpirationDates(), optionChain.getExpirationDates());
        assertIterableEquals(yfOptionChainResult.getStrikes(), optionChain.getStrikes());

        // Underlying info & Quote
        UnderlyingInfo underlyingInfo = optionChain.getUnderlyingInfo();
        assertNotNull(underlyingInfo);

        assertEquals("SPY", underlyingInfo.getSymbol());
        assertEquals("SPDR S&P 500", underlyingInfo.getShortName());
        assertEquals("SPDR S&P 500 ETF Trust", underlyingInfo.getLongName());
        assertEquals("ETF", underlyingInfo.getQuoteType());
        assertEquals("USD", underlyingInfo.getCurrency());
        assertEquals("US", underlyingInfo.getRegion());
        assertEquals("en-US", underlyingInfo.getLanguage());
        assertEquals("us_market", underlyingInfo.getMarket());
        assertEquals(917782016, underlyingInfo.getSharesOutstanding());
        assertEquals("Nasdaq Real Time Price", underlyingInfo.getQuoteSourceName());
//        assertEquals(, underlyingInfo.getFirstTradeDateMilliseconds());
//        private Long firstTradeDateMilliseconds;//728317800000, => Friday 29 January 1993 14:30:00 (EST)
        assertEquals(LocalDateTime.parse("1993-01-29T09:30:00"), underlyingInfo.getFirstTradeDateTime());
        assertEquals("NYSEArca", underlyingInfo.getFullExchangeName()); // Core Trading Session: 9:30 a.m. to 4:00 p.m. ET
        assertEquals("PCX", underlyingInfo.getExchange());
        assertEquals("CLOSED", underlyingInfo.getMarketState());
        assertEquals("America/New_York", underlyingInfo.getExchangeTimezoneName());
        assertEquals("EST", underlyingInfo.getExchangeTimezoneShortName());
        assertEquals(0, underlyingInfo.getExchangeDataDelayedBy());
        assertEquals(-18000000, underlyingInfo.getGmtOffSetMilliseconds());

        List<UnderlyingQuote> underlyingQuotes = underlyingInfo.getUnderlyingQuotes();
        assertNotNull(underlyingQuotes);
        assertEquals(1, underlyingQuotes.size());
        UnderlyingQuote underlyingQuote = underlyingQuotes.get(0);
        assertEquals("SPY", underlyingQuote.getSymbol());
        assertEquals(LocalDate.parse("2020-12-31"), underlyingQuote.getQuoteDate());
        assertEquals(374.4, underlyingQuote.getBid());
        assertEquals(374.4, underlyingQuote.getAsk());
        assertEquals(9, underlyingQuote.getBidSize());
        assertEquals(11, underlyingQuote.getAskSize());
        assertEquals(343140335616L, underlyingQuote.getMarketCap());
        assertEquals(1.89001, underlyingQuote.getRegularMarketChange());
        assertEquals(0.508082, underlyingQuote.getRegularMarketChangePercent());
        assertEquals(LocalDateTime.parse("2020-12-31T16:00:02"), underlyingQuote.getRegularMarketTime()); //1609448402, 31.12.2020 21:00:02 (EST)
        assertEquals(373.88, underlyingQuote.getRegularMarketPrice());
        assertEquals(374.65, underlyingQuote.getRegularMarketDayHigh());
        assertEquals("371.232 - 374.65", underlyingQuote.getRegularMarketDayRange());
        assertEquals(371.232, underlyingQuote.getRegularMarketDayLow());
        assertEquals(55516031L, underlyingQuote.getRegularMarketVolume());
        assertEquals(371.99, underlyingQuote.getRegularMarketPreviousClose());
        assertEquals(371.78, underlyingQuote.getRegularMarketOpen());
        assertEquals(0.0374121, underlyingQuote.getPostMarketChangePercent());
        assertEquals(LocalDateTime.parse("2020-12-31T19:59:38"), underlyingQuote.getPostMarketTime());    //1609462778, 01.01.2021 00:59:38 (EST)
        assertEquals(374.39, underlyingQuote.getPostMarketPrice());
        assertEquals(0.140015, underlyingQuote.getPostMarketChange());
        assertEquals(69860170L, underlyingQuote.getAverageDailyVolume3Month());
        assertEquals(49412620L, underlyingQuote.getAverageDailyVolume10Day());
        assertEquals(155.62001, underlyingQuote.getFiftyTwoWeekLowChange());
        assertEquals(0.7130029, underlyingQuote.getFiftyTwoWeekLowChangePercent());
        assertEquals("218.26 - 378.46", underlyingQuote.getFiftyTwoWeekRange());
        assertEquals(-4.5799866, underlyingQuote.getFiftyTwoWeekHighChange());
        assertEquals(-0.01210164, underlyingQuote.getFiftyTwoWeekHighChangePercent());
        assertEquals(218.26, underlyingQuote.getFiftyTwoWeekLow());
        assertEquals(378.46, underlyingQuote.getFiftyTwoWeekHigh());
        assertEquals(14.03, underlyingQuote.getYtdReturn());
        assertEquals(4.07, underlyingQuote.getTrailingThreeMonthReturns());
        assertEquals(3.88, underlyingQuote.getTrailingThreeMonthNavReturns());
        assertEquals(366.16095, underlyingQuote.getFiftyDayAverage());
        assertEquals(7.719055, underlyingQuote.getFiftyDayAverageChange());
        assertEquals(0.021081043, underlyingQuote.getFiftyDayAverageChangePercent());
        assertEquals(340.54218, underlyingQuote.getTwoHundredDayAverage());
        assertEquals(33.33783, underlyingQuote.getTwoHundredDayAverageChange());
        assertEquals(0.09789633, underlyingQuote.getTwoHundredDayAverageChangePercent());

        // OptionContract & Quotes
        List<OptionContract> optionContracts = optionChain.getOptionContracts();
        assertEquals(185, optionContracts.size());

        // CALL
        OptionContract lastCallContract = optionContracts.get(97);
        assertEquals("SPY210104C00470000", lastCallContract.getContractSymbol());
        assertEquals("SPY", lastCallContract.getUnderlyingSymbol());
        assertEquals(LocalDate.parse("2021-01-04"), lastCallContract.getExpiration());
        assertEquals(OptionContract.OptionType.CALL, lastCallContract.getOptionType());
        assertEquals(470.0, lastCallContract.getStrike());
        assertEquals("USD", lastCallContract.getCurrency());
        assertEquals("REGULAR", lastCallContract.getContractSize());

        List<OptionQuote> lastCallContractQuotes = lastCallContract.getQuotes();
        assertEquals(1, lastCallContractQuotes.size());
        OptionQuote lastCallContractQuote = lastCallContractQuotes.get(0);
        assertEquals("SPY210104C00470000", lastCallContractQuote.getContractSymbol());
        assertEquals(LocalDate.parse("2020-12-31"), lastCallContractQuote.getQuoteDate());
        assertEquals(LocalDateTime.parse("2020-12-21T20:08:24"), lastCallContractQuote.getLastTradeDate()); // 1608581304
        assertEquals(0.01, lastCallContractQuote.getLastPrice());
        assertEquals(0.0, lastCallContractQuote.getChange());
        assertEquals(0.0, lastCallContractQuote.getPercentChange());
        assertEquals(0.0, lastCallContractQuote.getBid());
        assertEquals(0.01, lastCallContractQuote.getAsk());
        assertEquals(1, lastCallContractQuote.getVolume());  // Not in JSON, set default in YF class
        assertEquals(5, lastCallContractQuote.getOpenInterest());
        assertEquals(0.9687503125, lastCallContractQuote.getImpliedVolatility(), 0.0001);
        assertEquals(false, lastCallContractQuote.getInTheMoney());

        // PUT
        OptionContract lastPutContract = optionContracts.get(184);
        assertEquals("SPY210104P00450000", lastPutContract.getContractSymbol());
        assertEquals("SPY", lastPutContract.getUnderlyingSymbol());
        assertEquals(LocalDate.parse("2021-01-04"), lastPutContract.getExpiration());
        assertEquals(OptionContract.OptionType.PUT, lastPutContract.getOptionType());
        assertEquals(450.0, lastPutContract.getStrike());
        assertEquals("USD", lastPutContract.getCurrency());
        assertEquals("REGULAR", lastPutContract.getContractSize());

        List<OptionQuote> lastPutContractQuotes = lastPutContract.getQuotes();
        assertEquals(1, lastPutContractQuotes.size());
        OptionQuote lastPutContractQuote = lastPutContractQuotes.get(0);
        assertEquals("SPY210104P00450000", lastPutContractQuote.getContractSymbol());
        assertEquals(LocalDate.parse("2020-12-31"), lastPutContractQuote.getQuoteDate());
        assertEquals(LocalDateTime.parse("2020-12-22T20:25:41"), lastPutContractQuote.getLastTradeDate());
        assertEquals(82.91, lastPutContractQuote.getLastPrice());
        assertEquals(0.0, lastPutContractQuote.getChange());
        assertEquals(0.0, lastPutContractQuote.getPercentChange());
        assertEquals(75.72, lastPutContractQuote.getBid());
        assertEquals(76.13, lastPutContractQuote.getAsk());
        assertEquals(0, lastPutContractQuote.getVolume());  // Not in JSON, set default in YF class
        assertEquals(0, lastPutContractQuote.getOpenInterest());
        assertEquals(0.8437515624999999, lastPutContractQuote.getImpliedVolatility(), 0.0001);
        assertEquals(true, lastPutContractQuote.getInTheMoney());

    }

    @Test
    void convert_list_of_seconds_to_list_of_dates() {
        List<Long> secondsList = Arrays.asList( 1609718400L, 1609891200L, 1610064000L, 1610323200L );
        List<LocalDate> expectedDatesList = Arrays.asList(
                LocalDate.parse("2021-01-04"),
                LocalDate.parse("2021-01-06"),
                LocalDate.parse("2021-01-08"),
                LocalDate.parse("2021-01-11")
        );
        List<LocalDate> actualDateList = YFv7OptionChain.dateOfEpochSecond(secondsList);
        assertIterableEquals(expectedDatesList, actualDateList);
    }
    @Test
    void full_list_of_dates() {
        List<LocalDate> actualDateList = YFv7OptionChain.dateOfEpochSecond(expirationDatesAsEpochSeconds());
        assertIterableEquals(expectedExpirationDates(), actualDateList);
    }

    private List<Long> expirationDatesAsEpochSeconds() {
        long[] expirationDates = new long[] {   1609718400, 1609891200, 1610064000, 1610323200, 1610496000,
                                        1610668800, 1611014400, 1611100800, 1611273600, 1611532800,
                                        1611705600, 1611878400, 1612137600, 1612483200, 1613692800,
                                        1616112000, 1617148800, 1618531200, 1621555200, 1623974400,
                                        1625011200, 1631836800, 1632960000, 1639699200, 1642723200,
                                        1647561600, 1655424000, 1663286400, 1671148800, 1674172800, 1702598400};
        return Arrays.stream(expirationDates).boxed().collect(Collectors.toList());
    }

    private List<LocalDate> expectedExpirationDates() {
        return Arrays.asList(
                LocalDate.parse("2021-01-04"), LocalDate.parse("2021-01-06"), LocalDate.parse("2021-01-08"),
                LocalDate.parse("2021-01-11"), LocalDate.parse("2021-01-13"), LocalDate.parse("2021-01-15"),
                LocalDate.parse("2021-01-19"), LocalDate.parse("2021-01-20"), LocalDate.parse("2021-01-22"),
                LocalDate.parse("2021-01-25"), LocalDate.parse("2021-01-27"), LocalDate.parse("2021-01-29"),
                LocalDate.parse("2021-02-01"), LocalDate.parse("2021-02-05"), LocalDate.parse("2021-02-19"),
                LocalDate.parse("2021-03-19"), LocalDate.parse("2021-03-31"), LocalDate.parse("2021-04-16"),
                LocalDate.parse("2021-05-21"), LocalDate.parse("2021-06-18"), LocalDate.parse("2021-06-30"),
                LocalDate.parse("2021-09-17"), LocalDate.parse("2021-09-30"), LocalDate.parse("2021-12-17"),
                LocalDate.parse("2022-01-21"), LocalDate.parse("2022-03-18"), LocalDate.parse("2022-06-17"),
                LocalDate.parse("2022-09-16"), LocalDate.parse("2022-12-16"), LocalDate.parse("2023-01-20"),
                LocalDate.parse("2023-12-15")
        );
    }


}