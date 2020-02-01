package com.robertozagni.SPYTM.data.collector.downloader;

import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;

import java.util.List;
import java.util.Map;

public interface Downloader {
    /**
     * Donloads the default TimeSerie of the given type for each of the provided symbols.
     *
     * @param quoteType The type of quote desired
     * @param symbols The symbols for which to download the quotes
     * @return A map that maps every passed symbol with the time serie retrieved for it.
     */
    Map<String, TimeSerie> download(QuoteType quoteType, List<String> symbols);
}
