package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @IdClass(TimeSerie.TimeSerieId.class)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TimeSerieMetadata {

    @Id @Enumerated(EnumType.STRING) private QuoteProvider provider;
    @Id @Enumerated(EnumType.STRING) private QuoteType quotetype;
    @Id @Column(length = 30) private String symbol;

    private String description;
    private String timeZone;
    private String lastRefreshed;
}
