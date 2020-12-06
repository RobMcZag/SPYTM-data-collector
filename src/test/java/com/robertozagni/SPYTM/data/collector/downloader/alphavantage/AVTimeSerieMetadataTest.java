package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AVTimeSerieMetadataTest {

    @Test
    void can_convert_to_TimeSerieMetadata() {
        AVTimeSerieMetadata md = makeTestAVTImeSerieMetadata();

        TimeSerieMetadata tsmd = md.toTimeSerieMetadata();

        assertEquals(QuoteProvider.ALPHA_VANTAGE, tsmd.getProvider());
        assertEquals(QuoteType.WEEKLY, tsmd.getQuotetype());
        assertEquals(md.getSymbol(), tsmd.getSymbol());
        assertEquals(md.getSeriesInfo(), tsmd.getDescription());
        assertEquals(md.getLastRefreshed(), tsmd.getLastRefreshed());
        assertEquals(md.getTimeZone(), tsmd.getTimeZone());
    }

    // TODO add more tests for wrong or missing series info

    /**
     * Creates a new AVTimeSerieMetadata object with some non null values.
     * @return a new AVTimeSerieMetadata
     */
    static AVTimeSerieMetadata makeTestAVTImeSerieMetadata() {
        return new AVTimeSerieMetadata(
                AVTimeSerieMetadata.SeriesInfoText.WEEKLY,
                "APPL",
                "2020-09-27",
                "compact",
                "US/SomePlace");
    }
}