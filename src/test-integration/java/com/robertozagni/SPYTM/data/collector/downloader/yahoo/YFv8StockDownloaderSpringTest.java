package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.robertozagni.SPYTM.data.collector.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class YFv8StockDownloaderSpringTest {

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private YFv8StockDownloader downloader;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        downloader = new YFv8StockDownloader(restTemplate);
    }

    @Test
    void can_receive_mock_response() throws IOException, URISyntaxException {
        File file = ResourceUtils.getFile("classpath:static/yahoo/chart_v8_APPL_w_DIV_SPLIT.json");
        String content = new String(Files.readAllBytes(file.toPath()));

        String symbol = "AAPL";

        DownloadRequest downloadRequest = new DownloadRequest(
                QuoteType.DAILY,
                QuoteProvider.YAHOO_FINANCE,
                DownloadRequest.getDefaultDownloadSize(),
                Collections.singletonList(symbol)
        );

        String url = YFv8StockDownloader.getBASE_URL_HTTP11() +
                     YFv8StockDownloader.getPRICE_URL_TEMPLATE() +
                     symbol +
                     "?" +
                     "symbol=" + symbol +
                     "&period1=0" +
                     "&period2=999999999" +
                     "&interval=1d";

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(content)
        );

        Map<String, TimeSerie> download = downloader.download(downloadRequest);
        assertNotNull(download);
    }
}