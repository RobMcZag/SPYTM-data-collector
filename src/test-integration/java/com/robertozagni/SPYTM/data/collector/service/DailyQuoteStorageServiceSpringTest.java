package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloaderTestHelper;
import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.repository.DailyQuoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(args = {"TEST"} )
class DailyQuoteStorageServiceSpringTest {

    @Autowired
    private DailyQuoteRepository realDailyQuoteRepository;

    private DailyQuoteStorageService dailyQuoteStorageService;

    @BeforeEach
    void init() {
        dailyQuoteStorageService = new DailyQuoteStorageService(realDailyQuoteRepository);
    }


    @Test
    void saveAllQuotes() throws IOException {
        TimeSerie msftTimeSerie = removeSomeQuotesFromTimeSerieForSpeed(
                AlphaVantageDownloaderTestHelper.loadSampleMSFT_AVTimeSerie().toModel());

        dailyQuoteStorageService.saveAllQuotes(msftTimeSerie);

        for (DailyQuote quote: msftTimeSerie.getData().values()) {
            Optional<DailyQuote> optQuote = realDailyQuoteRepository.findById(quote.getId());

            assertTrue(optQuote.isPresent());
            assertEquals(quote.getId(), optQuote.get().getId());
            assertEquals(quote, optQuote.get());
        }
    }

    private TimeSerie removeSomeQuotesFromTimeSerieForSpeed(TimeSerie timeSerie) {
        Map<String, DailyQuote> timeSerieData = new HashMap<>();
        for (String date: timeSerie.getData().keySet()) {
            if (date.compareTo("2020-01-15") >= 0) {
                timeSerieData.put(date, timeSerie.getData().get(date));
            }
        }
        timeSerie.setData(timeSerieData);
        return timeSerie;
    }

}