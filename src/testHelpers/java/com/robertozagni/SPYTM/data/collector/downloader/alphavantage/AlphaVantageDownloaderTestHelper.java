package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

public class AlphaVantageDownloaderTestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static AVTimeSerie loadSampleMSFT_AVTimeSerie() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/alphavantage/MSFT_DAILY_ADJ.json");
        return objectMapper.readValue(file, AVTimeSerie.class);
    }
    public static TimeSerie loadSampleMSFT_TimeSerie() throws IOException {
        return loadSampleMSFT_AVTimeSerie().toModel();
    }

}
