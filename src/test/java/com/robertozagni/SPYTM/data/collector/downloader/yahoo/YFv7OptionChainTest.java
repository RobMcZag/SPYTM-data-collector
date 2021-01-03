package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

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
}