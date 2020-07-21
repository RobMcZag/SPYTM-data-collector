package com.robertozagni.SPYTM.data.collector.controller;

import com.robertozagni.SPYTM.data.collector.model.DownloadRequest;
import com.robertozagni.SPYTM.data.collector.model.DownloadSize;
import com.robertozagni.SPYTM.data.collector.service.DataCollectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RestController
public class DataCollectorController {
    private final DataCollectorService dataCollectorService;

    public DataCollectorController(DataCollectorService dataCollectorService) {
        this.dataCollectorService = dataCollectorService;
    }

    @GetMapping("/download/{symbol}")
    public DownloadRequest download(@PathVariable String symbol) {
        DownloadRequest downloadRequest = new DownloadRequest(
                DownloadRequest.getDefaultQuoteType(),
                DownloadRequest.getDefaultQuoteProvider(),
                DownloadRequest.getDefaultDownloadSize(),
                Arrays.asList(DownloadRequest.tokenize(symbol))
        );

        dataCollectorService.downloadAndSave(downloadRequest);

        return downloadRequest;
    }

    @GetMapping("/downloadFull/{symbol}")
    public DownloadRequest downloadFull(@PathVariable String symbol) {
        DownloadRequest downloadRequest = new DownloadRequest(
                DownloadRequest.getDefaultQuoteType(),
                DownloadRequest.getDefaultQuoteProvider(),
                DownloadSize.FULL,
                Arrays.asList(DownloadRequest.tokenize(symbol))
        );

        dataCollectorService.downloadAndSave(downloadRequest);

        return downloadRequest;
    }
}
