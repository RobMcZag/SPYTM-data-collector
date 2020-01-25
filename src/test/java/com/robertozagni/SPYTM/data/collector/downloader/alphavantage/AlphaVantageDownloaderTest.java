package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AlphaVantageDownloaderTest {

    @Test @Disabled("Test not yet implemented.")
    void mapAVDataToModel() {
        // TODO Implement
    }

    @Test
    void can_convert_from_trading_date_to_UTCTime() {
        String tradingDate = "2020-01-23";
        String timeZone = "US/Eastern";     // 5 hours after (+) UTC in Jan
        LocalDateTime UTC_DateTime = AlphaVantageDownloader.toUTCTime(tradingDate, timeZone);
        System.out.println("UTC date time is " + UTC_DateTime);
        LocalDateTime expected = LocalDateTime.of(2020, 01, 23, 0 + 5, 0, 0);
        assertEquals(expected, UTC_DateTime);
    }

    @Test
    void can_convert_from_trading_time_to_UTCTime() {
        String tradingDate = "2020-01-24 15:45:00";
        String timeZone = "US/Eastern";     // 5 hours after (+) UTC in Jan
        LocalDateTime UTC_DateTime = AlphaVantageDownloader.toUTCTime(tradingDate, timeZone);
        System.out.println("UTC date time is " + UTC_DateTime);
        LocalDateTime expected = LocalDateTime.of(2020, 01, 24, 15 + 5, 45, 0);
        assertEquals(expected, UTC_DateTime);
    }

}