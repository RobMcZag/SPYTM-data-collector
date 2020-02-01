package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerieMetadata;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AVTimeSerieMetadataTest {

    @Test
    void can_convert_to_TimeSerieMetadata() {
        AVTimeSerieMetadata md = makeTestAVTImeSerieMetadata();

        TimeSerieMetadata tsmd = md.toTimeSerieMetadata();

        assertEquals(md.getSeriesInfo(), tsmd.getSeriesInfo());
        assertEquals(md.getSymbol(), tsmd.getSymbol());
        assertEquals(md.getLastRefreshed(), tsmd.getLastRefreshed());
        assertEquals(md.getOutputSize(), tsmd.getOutputSize());
        assertEquals(md.getTimeZone(), tsmd.getTimeZone());
    }

    /**
     * Creates a new AVTimeSerieMetadata object with some non null values.
     * @return a new AVTimeSerieMetadata
     */
    static AVTimeSerieMetadata makeTestAVTImeSerieMetadata() {
        return new AVTimeSerieMetadata(
                "Daily data",
                "APPL",
                "2020-09-27",
                "compact",
                "US/SomePlace");
    }
}