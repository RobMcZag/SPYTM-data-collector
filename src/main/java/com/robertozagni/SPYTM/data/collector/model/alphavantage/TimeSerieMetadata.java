package com.robertozagni.SPYTM.data.collector.model.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import static com.robertozagni.SPYTM.data.collector.model.alphavantage.TimeSerieMetadataFieldNames.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TimeSerieMetadata {
    @JsonProperty(value = SERIES_INFO) private String seriesInfo;
    @JsonProperty(value = SYMBOL) private String symbol;
    @JsonProperty(value = LAST_REFRESHED) private String lastRefreshed;
    @JsonProperty(value = OUTPUT_SIZE) private String outputSize;
    @JsonProperty(value = TIME_ZONE) private String timeZone;
}
