package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.model.*;
import com.robertozagni.SPYTM.data.datalake.service.SnowflakeStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service to download, store locally and eventually push to the data lake
 * the quotes for one or more stock symbols.
 *
 * Downloads the requested time-serie for the requested symbols.
 * Stores the downloaded TimeSeries in the local DB.
 * Pushes the data to the data lake.
 *
 * It also acts as a runner with the initial command line arguments:
 *      [QuoteType] [QuoteProvider] [DownloadSize] Symbol1 Symbol2 ...
 * All arguments are parsed to check if they identify a download parameter,
 * i.e. QuoteType or QuoteProvider or DownloadSize.
 * All arguments not identifying a parameter are considered a security symbol to download data for.
 *
 */
@Slf4j
@Service
public class DataCollectorService implements CommandLineRunner {

    private final DailyQuoteUpdateService dailyQuoteUpdateService;
    private final SnowflakeStorageService snowflakeStorageService;

    @Autowired
    public DataCollectorService(
            DailyQuoteUpdateService dailyQuoteUpdateService,
            SnowflakeStorageService snowflakeStorageService) {
        this.dailyQuoteUpdateService = dailyQuoteUpdateService;
        this.snowflakeStorageService = snowflakeStorageService;
    }

    /**
     * Collect and parse the arguments passed, invoking the right downloader.
     * @param args incoming main method arguments<BR>
     * Example: <pre>command TIME_SERIES_DAILY_ADJUSTED MSFT AAPL BA </pre>
     * will retrieve "daily adjusted data" for Microsoft, Apple and Boeing <BR>
     * TIME_SERIES_DAILY_ADJUSTED is the name of the desired data series, that also picks the data provider; <BR>
     * MSFT AAPL BA exemplify one or more stock symbols;
     */
    @Override
    public void run(String... args) {

        List<DownloadRequest> downloadRequestList = new ArrayList<>();
        if (args.length > 0) {
            downloadRequestList.add( DownloadRequest.parseArgs(args) );
        } else {
            downloadRequestList.add(
                dailyQuoteUpdateService.updateDailyQuotes(
                    QuoteProvider.YAHOO_FINANCE,
                    QuoteType.DAILY_ADJUSTED,
                    DownloadSize.LATEST)
            );
//            downloadRequestList.add(
//                dailyQuoteUpdateService.updateDailyQuotes(
//                    QuoteProvider.ALPHA_VANTAGE,
//                    QuoteType.DAILY_ADJUSTED,
//                    DownloadSize.LATEST)
//            );
        }

        for (DownloadRequest downloadRequest: downloadRequestList) {
            Map<String, TimeSerie> timeSeries = downloadAndSave(downloadRequest);

            if (snowflakeStorageService.isEnabled()) {
                loadToDatalake(timeSeries);
            }
        }

    }

    public Map<String, TimeSerie> downloadAndSave(DownloadRequest downloadRequest) {
        return dailyQuoteUpdateService.downloadAndSave(downloadRequest);
    }

    public void loadToDatalake(Map<String, TimeSerie> timeSeries) {
        timeSeries.values().forEach(
                (TimeSerie serie) -> {
                    snowflakeStorageService.load(serie);
                    log.info(String.format("Loaded into SF %d %s quotes for %s from %s!",
                            serie.getData().size(),
                            serie.getMetadata().getQuotetype(),
                            serie.getMetadata().getSymbol(),
                            serie.getMetadata().getProvider()) );
                }
        );
    }


}
