package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

public class YahooStockDownloaderTestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static YFv8TimeSerie loadSampleAAPL_YFv8TimeSerie() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/yahoo/chart_v8_APPL_w_DIV_SPLIT.json");
        return objectMapper.readValue(file, YFv8TimeSerie.class);
    }
//    public static TimeSerie loadSampleMSFT_TimeSerie() throws IOException {
//        return loadSampleMSFT_AVTimeSerie().toModel();
//    }

}
