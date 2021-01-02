package com.robertozagni.SPYTM.data.collector.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A request to download data for one or more symbols.
 *
 * The request represents multiple dimensions that describe what data to download
 * and from where, in addition to which symbols to download the data for.
 *
 */
@AllArgsConstructor
@Getter
public class DownloadRequest {
    private static final @Getter QuoteType defaultQuoteType = QuoteType.DAILY_ADJUSTED;
    private static final @Getter QuoteProvider defaultQuoteProvider = QuoteProvider.YAHOO_FINANCE;
    private static final @Getter DownloadSize defaultDownloadSize = DownloadSize.LATEST;

    private final QuoteType quoteType;
    private final QuoteProvider quoteProvider;
    private final DownloadSize downloadSize;
    private final List<String> symbols;

    /**
     * Parses the provided argument array (from command line or tokenize method)
     * to identify tokens representing QuoteType, QuoteProvider and DownloadSize
     * selection or just symbols to be downloaded.
     * All arguments not recognised as selections are considered symbols to be downloaded.
     * Argument order is not relevant for selection discovery, but the latest
     * selection for each dimension wil be used.
     * @param args The list of strings to parse into a request
     * @return The download request with selected dimensions and remaining symbols.
     */
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

            if(! arg.equalsIgnoreCase("TEST")) {
                symbols.add(arg.toUpperCase(Locale.ROOT)); // Then it's a symbol ! Make it Locale independent Uppercase :)
            }
        }
        return new DownloadRequest(quoteType, quoteProvider, downloadSize, symbols);
    }

    /**
     * Breaks down a single string with tokens separated by comma ","
     * semicolon ";" or space " " into the individual tokens.
     * @param tokenString the string representing one or more tokens
     * @return the tokens split into an array with one token per position
     */
    public static String[] tokenize(String tokenString) {
        tokenString = tokenString.replaceAll(",", " ");
        tokenString = tokenString.replaceAll(";", " ");
        tokenString = tokenString.replaceAll(":", " ");
        return tokenString.split(" ");
    }


}
