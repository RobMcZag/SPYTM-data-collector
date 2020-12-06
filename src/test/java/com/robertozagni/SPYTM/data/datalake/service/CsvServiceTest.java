package com.robertozagni.SPYTM.data.datalake.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloaderTestHelper;
import com.robertozagni.SPYTM.data.collector.model.DailyQuote;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvServiceTest {

    private CsvService csvService;
    private CsvService.DailyQuoteMappingStrategy strategy;

    @BeforeEach
    void setUp() {
        csvService = new CsvService();
        strategy = new CsvService.DailyQuoteMappingStrategy();
    }

    @Test
    void quotesToCSV() throws IOException, CsvException {
        TimeSerie timeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_TimeSerie();
        String csv = csvService.quotesToCSV(timeSerie);
        assertNotNull(csv);

        CSVReader csvReader = new CSVReaderBuilder(new StringReader(csv)).build();
        List<String[]> lines = csvReader.readAll();
        assertEquals(timeSerie.getData().size(), lines.size() - 1); // The CSV also has the header in pos 0
        for (String[] line : lines) {
            if (strategy.isHeader(line)) {continue;}
            DailyQuote dailyQuoteFromCSV = strategy.populateNewBean(line);
            String dateStr =  line[CsvService.DailyQuoteMappingStrategy.DAILY_QUOTE_FIELD_LIST.indexOf("date")];
            DailyQuote dailyQuoteFromTS = timeSerie.getData().get(dateStr);
            assertEquals(dailyQuoteFromTS, dailyQuoteFromCSV);
        }
    }

    @Test
    void testCSVToQuotes() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        TimeSerie timeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_TimeSerie();
        String csv = csvService.quotesToCSV(timeSerie);

        Map<String, DailyQuote> quoteMap = csvService.csvToDailyQuotes(csv);
        assertNotNull(quoteMap);
        assertEquals(timeSerie.getData().size(), quoteMap.size());
        for (String date : quoteMap.keySet()) {
            assertEquals(timeSerie.getData().get(date), quoteMap.get(date));
        }
    }

    @Test
    void strategy_populates_bean_correctly() throws CsvDataTypeMismatchException {
        String[] line = {"ALPHA_VANTAGE","DAILY_ADJUSTED","MSFT","2019-10-28","144.4","145.67","143.51","144.19","34912902","143.7002","0.0","1.0"};
        DailyQuote dailyQuote = strategy.populateNewBean(line);
        assertNotNull(dailyQuote);
        assertEquals(QuoteProvider.ALPHA_VANTAGE, dailyQuote.getProvider());
        assertEquals(QuoteType.DAILY_ADJUSTED, dailyQuote.getQuotetype());
        assertEquals("MSFT", dailyQuote.getSymbol());
        assertEquals("2019-10-28", dailyQuote.getDate().toString());
        assertEquals(144.4, dailyQuote.getOpen(), 0.01);
        assertEquals(145.67, dailyQuote.getHigh(), 0.01);
        assertEquals(143.51, dailyQuote.getLow(), 0.01);
        assertEquals(144.19, dailyQuote.getClose(), 0.01);
        assertEquals(34912902, dailyQuote.getVolume());
        assertEquals(143.7002, dailyQuote.getAdjustedClose(), 0.01);
        assertEquals(0.0, dailyQuote.getDividendAmount(), 0.01);
        assertEquals(1.0, dailyQuote.getSplitCoefficient(), 0.01);
    }
    @Test
    void strategy_populate_handles_header() {
        String[] header = {"provider","quotetype","symbol","date","open","high","low","close","volume","adjustedClose","dividendAmount","splitCoefficient"};
        assertThrows(CsvDataTypeMismatchException.class, () -> strategy.populateNewBean(header));
    }

    @Test
    void csv_and_json_content_match() throws IOException {
        TimeSerie timeSerie = AlphaVantageDownloaderTestHelper.loadSampleMSFT_TimeSerie();

        File file = ResourceUtils.getFile("classpath:static/msft_quotes.csv");
        Map<String, DailyQuote> quoteMap = csvService.csvToDailyQuotes(new FileReader(file));

        assertNotNull(quoteMap);
        assertEquals(timeSerie.getData().size(), quoteMap.size());
        for (String date : quoteMap.keySet()) {
            assertEquals(timeSerie.getData().get(date), quoteMap.get(date));
        }

    }
}