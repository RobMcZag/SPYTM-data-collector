package com.robertozagni.SPYTM.data.collector.downloader;

import com.robertozagni.SPYTM.data.collector.model.DataSeries;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;

import java.util.List;
import java.util.Map;

public interface Downloader {
    Map<String, TimeSerie> download(DataSeries dataSeries, List<String> symbols);
}
