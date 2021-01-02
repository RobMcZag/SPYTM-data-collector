package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.model.*;
import com.robertozagni.SPYTM.data.collector.repository.DailyQuoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(args = {"TEST"} )
class DailyQuoteUpdateServiceSpringTest {

    @Autowired private DailyQuoteRepository realDailyQuoteRepository;
    @Autowired private DailyQuoteUpdateService dailyQuoteUpdateService;

    @Test
    void create_download_request_for_latest_stock() throws IOException {
        DownloadRequest downloadRequest = dailyQuoteUpdateService.updateDailyQuotes(
                QuoteProvider.YAHOO_FINANCE,
                QuoteType.DAILY_ADJUSTED,
                DownloadSize.LATEST);
        assertNotNull(downloadRequest);


        List<DailyQuote> latestQuoteByDate = realDailyQuoteRepository.getLatestQuotes();
        assertNotNull(latestQuoteByDate);
    }

}