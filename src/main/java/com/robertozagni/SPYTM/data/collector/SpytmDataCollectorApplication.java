package com.robertozagni.SPYTM.data.collector;

import com.robertozagni.SPYTM.data.collector.downloader.StockDataDownloaderRunner;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AlphaVantageDownloader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableTransactionManagement
public class SpytmDataCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpytmDataCollectorApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner createRunner(RestTemplate restTemplate) throws Exception {
		return new StockDataDownloaderRunner(new AlphaVantageDownloader(restTemplate));
	}

}
