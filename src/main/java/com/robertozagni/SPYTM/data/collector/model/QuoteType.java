package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * An enumeration listing the types of Quotes the system can handle.
 */
@AllArgsConstructor
public enum QuoteType {
    INTRADAY,
    DAILY,
    DAILY_ADJUSTED,
    WEEKLY,
    WEEKLY_ADJUSTED,
    MONTHLY,
    MONTHLY_ADJUSTED;
}




