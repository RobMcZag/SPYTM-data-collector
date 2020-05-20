package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;


@SpringBootTest
public class AlphaVantageDownloaderSpringTest {

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private AlphaVantageDownloader downloader;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        downloader = new AlphaVantageDownloader(restTemplate);
    }

    @Test
    void can_download_an_existing_serie() throws URISyntaxException, IOException {
        File file = ResourceUtils.getFile("classpath:static/alphavantage/MSFT_DAILY_ADJ.json");
        String content = new String(Files.readAllBytes(file.toPath()));

        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&outputsize=compact&apikey=SHGHQ9MPDSLJSKO5";
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(content)
                );

        Map<String, TimeSerie> download = downloader.download(QuoteType.DAILY, Collections.singletonList("MSFT"));
        assertNotNull(download);

        TimeSerie msft = download.get("MSFT");

//        "Meta Data": {
//            "1. Information": "Daily Time Series with Splits and Dividend Events",
//                    "2. Symbol": "MSFT",
//                    "3. Last Refreshed": "2020-01-23 13:02:11",
//                    "4. Output Size": "Compact",
//                    "5. Time Zone": "US/Eastern"
//        },
        assertEquals(msft.getMetadata().getSymbol(), "MSFT");
        assertEquals(msft.getMetadata().getDescription(), "Daily Time Series with Splits and Dividend Events");
        assertEquals(msft.getMetadata().getLastRefreshed(), "2020-01-23 13:02:11");
        assertEquals(msft.getMetadata().getTimeZone(), "US/Eastern");

        assertEquals(msft.getData().size(), 100);
//        "Time Series (Daily)": {
//            "2020-01-23": {
//                        "1. open": "166.1900",
//                        "2. high": "166.6200",
//                        "3. low": "165.2700",
//                        "4. close": "165.8300",
//                        "5. adjusted close": "165.8300",
//                        "6. volume": "7457748",
//                        "7. dividend amount": "0.0000",
//                        "8. split coefficient": "1.0000"
//            },
        assertEquals(msft.getData().get("2020-01-23").getOpen(),166.19, 0.1);
        assertEquals(msft.getData().get("2020-01-23").getHigh(), 166.62, 0.1);
        assertEquals(msft.getData().get("2020-01-23").getLow(), 165.27 , 0.1);
        assertEquals(msft.getData().get("2020-01-23").getClose(), 165.83, 0.1);
        assertEquals(msft.getData().get("2020-01-23").getAdjustedClose(), 165.83, 0.1);
        assertEquals(msft.getData().get("2020-01-23").getVolume(), 7457748);
        assertEquals(msft.getData().get("2020-01-23").getDividendAmount(),0, 0.1);
        assertEquals(msft.getData().get("2020-01-23").getSplitCoefficient(), 1, 0.1);

    }


}
