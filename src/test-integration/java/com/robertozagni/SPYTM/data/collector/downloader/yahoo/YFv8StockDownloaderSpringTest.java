package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.robertozagni.SPYTM.data.collector.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(args = {"TEST"} )
class YFv8StockDownloaderSpringTest {

    @Autowired
    private RestTemplate restTemplate;

    private ClientHttpRequestFactory originalRequestFactory;
    private MockRestServiceServer mockServer;
    private YFv8StockDownloader downloader;

    @BeforeEach
    void setup() {
        originalRequestFactory = restTemplate.getRequestFactory();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        downloader = new YFv8StockDownloader(restTemplate);
    }
    @AfterEach
    void resetRestTemplate() {
        restTemplate.setRequestFactory(originalRequestFactory);
    }

    @Test
    void can_receive_mock_response() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/yahoo/chart_v8_APPL_w_DIV_SPLIT.json");
        String content = new String(Files.readAllBytes(file.toPath()));

        String symbol = "AAPL";

        DownloadRequest downloadRequest = new DownloadRequest(
                QuoteType.DAILY,
                QuoteProvider.YAHOO_FINANCE,
                DownloadRequest.getDefaultDownloadSize(),
                Collections.singletonList(symbol)
        );

        mockServer.expect(ExpectedCount.once(),
                          MockRestRequestMatchers.anything()
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(content)
        );

        Map<String, TimeSerie> download = downloader.download(downloadRequest);
        assertNotNull(download);
        assertNotNull(download.get(symbol));
        assertNotNull(download.get(symbol).getMetadata());
        assertNotNull(download.get(symbol).getData());

        assertEquals(symbol, download.get(symbol).getMetadata().getSymbol());
        Map<String, DailyQuote> quoteMap = download.get(symbol).getData();
        assertEquals(10076, quoteMap.size());
        assertNotNull(quoteMap.get("2020-11-25"));
        assertEquals(symbol, quoteMap.get("2020-11-25").getSymbol());
        assertEquals(LocalDate.parse("2020-11-25"), quoteMap.get("2020-11-25").getDate());
        assertEquals(115.55, quoteMap.get("2020-11-25").getQuote().getOpen(), 0.001);
        assertEquals(1.0, quoteMap.get("2020-11-25").getSplitCoefficient(), 0.001);
    }
}