package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

public class AlphaVantageDownloaderTestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static AVTimeSerie loadSampleMSFT_AVTimeSerie() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/alphavantage/MSFT_DAILY_ADJ.json");
        return objectMapper.readValue(file, AVTimeSerie.class);
    }

}
