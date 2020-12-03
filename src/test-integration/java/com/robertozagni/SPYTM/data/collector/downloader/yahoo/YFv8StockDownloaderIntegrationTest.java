package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class YFv8StockDownloaderIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    private YFv8StockDownloader downloader;

    @BeforeEach
    void setup() {
        downloader = new YFv8StockDownloader(restTemplate);
    }

    @Test
    void can_receive_real_response() throws IOException, URISyntaxException {
        String symbol = "ISP.MI";
        DownloadRequest downloadRequest = new DownloadRequest(
                QuoteType.DAILY,
                QuoteProvider.YAHOO_FINANCE,
                DownloadRequest.getDefaultDownloadSize(),
                Collections.singletonList(symbol)
        );
        Map<String, TimeSerie> download = downloader.download(downloadRequest);
        assertNotNull(download);
    }

}