package com.robertozagni.SPYTM.data.collector;

import com.robertozagni.SPYTM.data.collector.downloader.StockDataDownloaderRunner;
import com.robertozagni.SPYTM.data.collector.repository.DailyQuoteRepository;
import com.robertozagni.SPYTM.data.collector.service.DailyQuoteStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories
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
	public CommandLineRunner createRunner(RestTemplate restTemplate, DailyQuoteRepository repository) throws Exception {
		return new StockDataDownloaderRunner(restTemplate, new DailyQuoteStorageService(repository));
	}

}
