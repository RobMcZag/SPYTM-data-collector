package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.robertozagni.SPYTM.data.collector.model.OptionChain;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
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

        OptionChain optionChain = yfOptionChain.toModel();
        assertNotNull(optionChain);
        assertEquals("SPY", optionChain.getSymbol());
        assertEquals(LocalDate.parse("2021-01-04"), optionChain.getExpirationDate());
        assertIterableEquals(expectedExpirationDates(), optionChain.getExpirationDates());
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