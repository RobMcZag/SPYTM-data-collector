package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.DataSeries;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieStockData;
import com.robertozagni.SPYTM.data.collector.model.alphavantage.AVStockData;
import com.robertozagni.SPYTM.data.collector.model.alphavantage.AVTimeSerie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AlphaVantageDownloader implements com.robertozagni.SPYTM.data.collector.downloader.Downloader {
    // TODO collect APIKey from config, ideally from env (not typed into config or source)
    private static final String API_KEY = "SHGHQ9MPDSLJSKO5";
    private static final String BASE_URL = "https://www.alphavantage.co/query";

    private final RestTemplate restTemplate;

    public AlphaVantageDownloader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, TimeSerie> download(DataSeries dataSeries, List<String> symbols) {
        Map<String, TimeSerie> result = new HashMap<>();
        for (String symbol: symbols) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("function", dataSeries.name())
                    .queryParam("symbol", symbol)
                    .queryParam("apikey", API_KEY);
            String url = builder.toUriString();

            log.debug("** Downloading " + url);
            try {
                AVTimeSerie data = restTemplate.getForObject(url, AVTimeSerie.class);
                assert data != null;
                result.put(symbol, mapAVDataToModel(data));
            } catch (RestClientException e) {
                log.error("Error while attemptiong to download data from " + url, e);
            }
        }
        return result;

    }

    static public TimeSerie mapAVDataToModel(AVTimeSerie avdata) {
        TimeSerieMetadata metaData = avdata.getMetadata().toTimeSerieMetadata();

        Map<String, TimeSerieStockData> timeSerieStockData = new HashMap<>();
        for (String tradingDate: avdata.getStockData().keySet()) {
            AVStockData sd = avdata.getStockData().get(tradingDate);
            sd.setDateTime(toUTCTime(tradingDate, metaData.getTimeZone()));
            timeSerieStockData.put(tradingDate, sd.toTimeSerieStockData());
        }
        return new TimeSerie(metaData, timeSerieStockData);

    }

    static public LocalDateTime toUTCTime(String tradingDate, String timeZone) {
        ZoneId originZone = ZoneId.of(timeZone);
        LocalDateTime originLDT = parseTradingDate(tradingDate);
        ZonedDateTime originZDT = ZonedDateTime.of(originLDT, originZone);
        return originZDT.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    private static LocalDateTime parseTradingDate(String tradingDate) {
        String tradingDateTime;
        if (tradingDate.contains(":")) {
            tradingDateTime = tradingDate;
        } else {
            tradingDateTime = tradingDate + AVTimeSerie.Constants.AT_MIDNIGHT;
        }
        return LocalDateTime.parse(tradingDateTime, AVTimeSerie.Constants.DATE_TIME_FORMAT);
    }

}
