package com.robertozagni.SPYTM.data.datalake.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.robertozagni.SPYTM.data.collector.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.*;

/**
 * A totally stateless service to convert between TimeSerie and CSV representation.
 */
@Service
public class CsvService {

    public String quotesToCSV(TimeSerie timeSerie) throws
            CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {

        try (Writer writer = new StringWriter()) {
            quotesToCSV(timeSerie.getData().values(), writer);
            return writer.toString();
        }
    }
    public void quotesToCSV(Collection<DailyQuote> quotes, Writer writer) throws CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException, IOException {

        StatefulBeanToCsv<DailyQuote> quotesToCSV = new StatefulBeanToCsvBuilder<DailyQuote>(writer)
                .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .withMappingStrategy(new DailyQuoteMappingStrategy())
                .withOrderedResults(true)
                .build();

        quotesToCSV.write(new ArrayList<>(quotes));
        writer.flush(); // leaves the closing to the owner
    }

    public Map<String, DailyQuote> csvToDailyQuotes(String csv){
        return csvToDailyQuotes(new StringReader(csv));
    }

    public Map<String, DailyQuote> csvToDailyQuotes(Reader reader){
        Map<String, DailyQuote> quoteMap = new HashMap<>();

        CsvToBean<DailyQuote> csvToBean = new CsvToBeanBuilder<DailyQuote>(reader)
                .withSkipLines(0)   // The strategy will ignore the header, eventually
                .withIgnoreLeadingWhiteSpace(true)
                .withMappingStrategy(new DailyQuoteMappingStrategy())
                .build();

        for (DailyQuote quote : csvToBean) {
            quoteMap.put(quote.getDate().toString(), quote);
        }
        return quoteMap;
    }

    /**
     * A mapping strategy specific for DailyQuotes.
     *
     * DailyQuotes have a few features, like Quote inner object and enum based fields, that defy the basic CSV
     * conversion and bean processing, so we built an ad hoc strategy.
     */
    public static class DailyQuoteMappingStrategy
                            extends HeaderNameBaseMappingStrategy<DailyQuote>
                            implements MappingStrategy<DailyQuote>  {

        public static final String[] DAILY_QUOTE_FIELD_ORDER = {
                "provider", "quotetype", "symbol", "date",
                "open", "high", "low", "close", "volume",
                "adjustedClose", "dividendAmount", "splitCoefficient"
                };
        public static final List<String> DAILY_QUOTE_FIELD_LIST =
            Arrays.asList(CsvService.DailyQuoteMappingStrategy.DAILY_QUOTE_FIELD_ORDER);

        private static final List<String> QUOTE_FIELDS = Arrays.asList("open", "high", "low", "close", "volume");

        /**
         * Buold the strategy setting the right type of the handled objects and the order of the fields.
         */
        public DailyQuoteMappingStrategy() {
            super();
            super.setType(DailyQuote.class);
            super.setColumnOrderOnWrite(new OrderedComparatorIgnoringCase(DAILY_QUOTE_FIELD_ORDER));
        }

        /**
         * Relaxed check to see if a line is of data or is the header.
         *
         * It just checks the first field of the line to be equal to the first element of the header.
         * @param line A csv line split into the fields
         * @return True if the line looks like an header line.
         */
        public boolean isHeader(String[] line) {
            return line[0].equals(DailyQuoteMappingStrategy.DAILY_QUOTE_FIELD_ORDER[0]);
        }

        @Override
        public String[] generateHeader(DailyQuote bean) {
            return DAILY_QUOTE_FIELD_ORDER;
        }

        /**
         * Takes a line of input from a CSV file and creates a bean out of it.
         *
         * @param line A line of input returned from {@link CSVReader}
         * @return A bean containing the converted information from the input
         * @throws CsvBeanIntrospectionException   Generally, if some part of the bean cannot
         *                                         be accessed and used as needed
         * @throws CsvDataTypeMismatchException    If conversion of the input to a
         *                                         field type fails
         * @since 4.2
         */
        @Override
        public DailyQuote populateNewBean(String[] line) throws
                CsvBeanIntrospectionException, CsvDataTypeMismatchException {

            if(isHeader(line)) {
                throw new CsvDataTypeMismatchException("This looks like the header row, not data.");
            }

            DailyQuote dailyQuote = new DailyQuote();
            dailyQuote.setProvider(QuoteProvider.valueOf(line[DAILY_QUOTE_FIELD_LIST.indexOf("provider")]));
            dailyQuote.setQuotetype(QuoteType.valueOf(line[DAILY_QUOTE_FIELD_LIST.indexOf("quotetype")]));
            dailyQuote.setSymbol(line[DAILY_QUOTE_FIELD_LIST.indexOf("symbol")]);
            dailyQuote.setDate(LocalDate.parse(line[DAILY_QUOTE_FIELD_LIST.indexOf("date")]));
            Quote quote = new Quote();
            quote.setOpen(Double.parseDouble(line[DAILY_QUOTE_FIELD_LIST.indexOf("open")]));
            quote.setHigh(Double.parseDouble(line[DAILY_QUOTE_FIELD_LIST.indexOf("high")]));
            quote.setLow(Double.parseDouble(line[DAILY_QUOTE_FIELD_LIST.indexOf("low")]));
            quote.setClose(Double.parseDouble(line[DAILY_QUOTE_FIELD_LIST.indexOf("close")]));
            quote.setVolume(Integer.parseInt(line[DAILY_QUOTE_FIELD_LIST.indexOf("volume")]));
            dailyQuote.setQuote(quote);
            dailyQuote.setAdjustedClose(Double.parseDouble(line[DAILY_QUOTE_FIELD_LIST.indexOf("adjustedClose")]));
            dailyQuote.setDividendAmount(Double.parseDouble(line[DAILY_QUOTE_FIELD_LIST.indexOf("dividendAmount")]));
            dailyQuote.setSplitCoefficient(Double.parseDouble(line[DAILY_QUOTE_FIELD_LIST.indexOf("splitCoefficient")]));
            return dailyQuote;
        }

        /**
         * Transmutes a bean instance into an array of {@link String}s to be written
         * to a CSV file.
         *
         * @param dailyQuote The bean to be transmuted
         * @return The converted values of the bean fields in the correct order,
         * ready to be passed to a {@link CSVWriter}
         */
        @Override
        public String[] transmuteBean(DailyQuote dailyQuote) throws CsvBeanIntrospectionException {
            String[] line = new String[DAILY_QUOTE_FIELD_ORDER.length];
            for (int i = 0; i < DAILY_QUOTE_FIELD_ORDER.length; i++) {
                String propertyName = DAILY_QUOTE_FIELD_ORDER[i];
                try {
                    if (QUOTE_FIELDS.contains(propertyName)) {
                        line[i] = BeanUtils.getProperty(dailyQuote.getQuote(), propertyName);
                    } else {
                        line[i] = BeanUtils.getProperty(dailyQuote, propertyName);
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new CsvBeanIntrospectionException(e.getMessage());
                }
            }
            return line;
        }

    }

    public static class OrderedComparatorIgnoringCase implements Comparator<String> {
        private List<String> predefinedOrder;

        public OrderedComparatorIgnoringCase(String[] predefinedOrder) {
            this.predefinedOrder = new ArrayList<>();
            for (String item : predefinedOrder) {
                this.predefinedOrder.add(item.toLowerCase());
            }
        }

        @Override
        public int compare(String o1, String o2) {
            return predefinedOrder.indexOf(o1.toLowerCase()) - predefinedOrder.indexOf(o2.toLowerCase());
        }
    }
}
