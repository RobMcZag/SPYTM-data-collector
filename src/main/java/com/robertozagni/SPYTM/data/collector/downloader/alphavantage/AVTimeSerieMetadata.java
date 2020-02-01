package com.robertozagni.SPYTM.data.collector.downloader.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import lombok.*;

import static com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerieMetadata.Constants.*;

@Data
@AllArgsConstructor
public class AVTimeSerieMetadata {

    @JsonProperty(value = SERIES_INFO) private String seriesInfo;
    @JsonProperty(value = SYMBOL) private String symbol;
    @JsonProperty(value = LAST_REFRESHED) private String lastRefreshed;
    @JsonProperty(value = OUTPUT_SIZE) private String outputSize;
    @JsonProperty(value = TIME_ZONE) private String timeZone;

    public TimeSerieMetadata toTimeSerieMetadata() {
        return new TimeSerieMetadata(seriesInfo, symbol, lastRefreshed, outputSize, timeZone);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constants {
        public static final String SERIES_INFO = "1. Information";
        public static final String SYMBOL = "2. Symbol";
        public static final String LAST_REFRESHED = "3. Last Refreshed";
        public static final String OUTPUT_SIZE = "4. Output Size";
        public static final String TIME_ZONE = "5. Time Zone";
    }
}
