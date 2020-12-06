package com.robertozagni.SPYTM.data.collector.downloader.yahoo;

import com.robertozagni.SPYTM.data.collector.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        assertNotNull(download.get(symbol));
        assertNotNull(download.get(symbol).getMetadata());
        assertNotNull(download.get(symbol).getData());

        assertEquals(symbol, download.get(symbol).getMetadata().getSymbol());

        Map<String, DailyQuote> quoteMap = download.get(symbol).getData();
        assertTrue(quoteMap.size() > 0);
        assertTrue(quoteMap.size() <= YFv8StockDownloader.DAYS_FOR_LATEST);

        Optional<String> optionalKey = quoteMap.keySet().stream().findAny();
        String quoteDate = optionalKey.get();
        assertNotNull(quoteMap.get(quoteDate));
        assertEquals(symbol, quoteMap.get(quoteDate).getSymbol());
        assertEquals(LocalDate.parse(quoteDate), quoteMap.get(quoteDate).getDate());
    }

}