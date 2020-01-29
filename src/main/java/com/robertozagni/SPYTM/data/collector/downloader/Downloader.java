package com.robertozagni.SPYTM.data.collector.downloader;

import com.robertozagni.SPYTM.data.collector.model.DataSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerie;

import java.util.List;
import java.util.Map;

public interface Downloader {
    Map<String, TimeSerie> download(DataSerie dataSerie, List<String> symbols);
}
