package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * An enumeration listing the known Data Series and their providers (inner enum DataService).
 */
@AllArgsConstructor
public enum DataSerie {
    TIME_SERIES_INTRADAY(DataProvider.APLPHA_VANTAGE),
    TIME_SERIES_DAILY(DataProvider.APLPHA_VANTAGE),
    TIME_SERIES_DAILY_ADJUSTED(DataProvider.APLPHA_VANTAGE),
    TIME_SERIES_WEEKLY(DataProvider.APLPHA_VANTAGE),
    TIME_SERIES_WEEKLY_ADJUSTED(DataProvider.APLPHA_VANTAGE),
    // .... TODO add all other values
    SYMBOL_SEARCH(DataProvider.APLPHA_VANTAGE),
    TEST_SERIE(DataProvider.TEST);

    @Getter private final DataProvider dataProvider;


    /**
     * To use the converter add the following annotation on the field (not for Id fields): <BR>
     * <pre>Convert(converter = DataSerie.JpaConverter.class)</pre>
     */
    @Converter
    public static class JpaConverter implements AttributeConverter<DataSerie, String> {
        @Override
        public String convertToDatabaseColumn(DataSerie attribute) {
            if(attribute == null) {
                return null;
            }
            return attribute.toString();
        }
        @Override
        public DataSerie convertToEntityAttribute(String dbData) {
            try {
                if (dbData == null) { return null; }
                return DataSerie.valueOf(dbData);
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }
    }

    /**
     * An enumeration listing the known Data Service
     */
    public enum DataProvider {
        APLPHA_VANTAGE, TEST;
    }
}


