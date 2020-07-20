package com.robertozagni.SPYTM.data.collector.downloader;

import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.model.QuoteType;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;

import java.util.List;
import java.util.Map;

public interface Downloader {
    /**
     * Donloads the requested TimeSerie of the given type for each of the provided symbols.
     *
     * @param downloadRequest The details of the download request
     * @return A map that maps every passed symbol with the time serie retrieved for it.
     */
    Map<String, TimeSerie> download(DownloadRequest downloadRequest);
}
