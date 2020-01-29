package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.DataSerie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataSerieJpaConverterTest {
    private DataSerie.JpaConverter dsJpaConverter = new DataSerie.JpaConverter();

    @Test
    void can_convert_the_enum_to_string(){
        String dataSerieDbValue = dsJpaConverter.convertToDatabaseColumn(DataSerie.TIME_SERIES_DAILY_ADJUSTED);
        assertEquals(dataSerieDbValue, DataSerie.TIME_SERIES_DAILY_ADJUSTED.toString());
    }
    @Test
    void can_convert_a_null_attribute_to_null(){
        String dataSerieDbValue = dsJpaConverter.convertToDatabaseColumn(null);
        assertNull(dataSerieDbValue);
    }

    @Test
    void can_convert_an_existing_string_into_enum(){
        DataSerie dataSerie = dsJpaConverter.convertToEntityAttribute(DataSerie.TIME_SERIES_DAILY_ADJUSTED.toString());
        assertEquals(dataSerie, DataSerie.TIME_SERIES_DAILY_ADJUSTED);
    }
    @Test
    void can_convert_a_non_existing_string_into_null(){
        DataSerie dataSerie = dsJpaConverter.convertToEntityAttribute("I am not a good value");
        assertNull(dataSerie);
    }
    @Test
    void can_convert_a_null_string_into_null(){
        DataSerie dataSerie = dsJpaConverter.convertToEntityAttribute(null);
        assertNull(dataSerie);
    }

}
