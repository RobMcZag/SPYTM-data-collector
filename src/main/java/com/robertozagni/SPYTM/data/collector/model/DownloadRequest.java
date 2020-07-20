package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class DownloadRequest {
    private static final @Getter QuoteType defaultQuoteType = QuoteType.DAILY_ADJUSTED;
    private static final @Getter QuoteProvider defaultQuoteProvider = QuoteProvider.APLPHA_VANTAGE;
    private static final @Getter DownloadSize defaultDownloadSize = DownloadSize.LATEST;

    private final QuoteType quoteType;
    private final QuoteProvider quoteProvider;
    private final DownloadSize downloadSize;
    private final List<String> symbols;

    public static DownloadRequest parseArgs(String[] args) {
        QuoteType quoteType = getDefaultQuoteType();
        QuoteProvider quoteProvider = getDefaultQuoteProvider();
        DownloadSize downloadSize = getDefaultDownloadSize();
        List<String> symbols = new ArrayList<>();

        for (String arg: args) {
            try {
                quoteType = QuoteType.valueOf(arg);
                continue;
            } catch (IllegalArgumentException ignored) { }   // Not a serie type
            try {
                quoteProvider = QuoteProvider.valueOf(arg);
                continue;
            } catch (IllegalArgumentException ignored) { }   // Not a provider
            try {
                downloadSize = DownloadSize.valueOf(arg);
                continue;
            } catch (IllegalArgumentException ignored) { }   // Not a download size

            symbols.add(arg);                                // Then it's a symbol ! :)
        }
        return new DownloadRequest(quoteType, quoteProvider, downloadSize, symbols);
    }

}
