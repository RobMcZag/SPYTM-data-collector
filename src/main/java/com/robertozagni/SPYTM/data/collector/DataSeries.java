package com.robertozagni.SPYTM.data.collector;

import com.robertozagni.SPYTM.data.collector.DataServices;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DataSeries {
    TIME_SERIES_INTRADAY(DataServices.APLPHA_VANTAGE),
    TIME_SERIES_DAILY(DataServices.APLPHA_VANTAGE),
    TIME_SERIES_DAILY_ADJUSTED(DataServices.APLPHA_VANTAGE),
    TIME_SERIES_WEEKLY(DataServices.APLPHA_VANTAGE),
    TIME_SERIES_WEEKLY_ADJUSTED(DataServices.APLPHA_VANTAGE),
    // .... TODO add all other values
    SYMBOL_SEARCH(DataServices.APLPHA_VANTAGE),
    TEST_SERIE(DataServices.TEST);

    @Getter private final DataServices datService;

}


