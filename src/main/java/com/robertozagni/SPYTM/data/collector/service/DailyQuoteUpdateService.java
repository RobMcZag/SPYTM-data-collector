package com.robertozagni.SPYTM.data.collector.service;

import com.robertozagni.SPYTM.data.collector.model.*;
import com.robertozagni.SPYTM.data.collector.repository.DailyQuoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DailyQuoteUpdateService {

    private final DailyQuoteRepository repository;
    private final DataDownloaderService dataDownloaderService;
    private final TimeSerieStorageService timeSerieStorageService;

    @Autowired
    public DailyQuoteUpdateService(
            DailyQuoteRepository repository,
            DataDownloaderService dataDownloaderService,
            TimeSerieStorageService timeSerieStorageService
            ) {
        this.repository = repository;
        this.dataDownloaderService = dataDownloaderService;
        this.timeSerieStorageService = timeSerieStorageService;
    }

    public DownloadRequest updateDailyQuotes(QuoteProvider provider, QuoteType quoteType, DownloadSize downloadSize) {

        List<String> symbols = new ArrayList<>();
        int downloadedDays = 60;
        if (provider == QuoteProvider.ALPHA_VANTAGE) { downloadedDays = 100; }
        LocalDate limitForLatestQuotes = LocalDate.now().minus(downloadedDays, ChronoUnit.DAYS);

        for (DailyQuote quote: repository.getLatestQuotes()) {
            if (quote.getQuotetype() == quoteType
                && quote.getProvider() == provider
                && (downloadSize == DownloadSize.FULL || quote.getDate().isAfter(limitForLatestQuotes) )
            ) {
                symbols.add(quote.getSymbol());
            }
        }

        return new DownloadRequest(quoteType, provider, downloadSize, symbols);
    }

    public Map<String, TimeSerie> downloadAndSave(DownloadRequest downloadRequest) {
        Map<String, TimeSerie> timeSeries = dataDownloaderService.downloadQuotes(downloadRequest);
        timeSeries.values().forEach(
                (TimeSerie serie) ->
                        log.info(String.format("Downloaded %d %s quotes for %s from %s!",
                                serie.getData().size(),
                                serie.getMetadata().getQuotetype(),
                                serie.getMetadata().getSymbol(),
                                serie.getMetadata().getProvider()) )
        );

        timeSeries.values().forEach(
                (TimeSerie serie) -> {
                    timeSerieStorageService.save(serie);
                    log.info(String.format("Saved in local DB %d %s quotes for %s from %s!",
                            serie.getData().size(),
                            serie.getMetadata().getQuotetype(),
                            serie.getMetadata().getSymbol(),
                            serie.getMetadata().getProvider()) );
                }
        );
        return timeSeries;
    }

}
