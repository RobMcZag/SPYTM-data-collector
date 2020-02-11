package com.robertozagni.SPYTM.data.datalake.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloaderTestHelper;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SnowflakeStorageServiceSpringTest {

    @Autowired private CsvService csvService;
    @Autowired private SnowflakeStorageService snowflakeStorageService;

    @Test
    void can_save_a_timeserie() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        TimeSerie timeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_TimeSerie();
        String csv = csvService.quotesToCSV(timeSerie);
        assertNotNull(csv);

        snowflakeStorageService.load(timeSerie);


    }
}