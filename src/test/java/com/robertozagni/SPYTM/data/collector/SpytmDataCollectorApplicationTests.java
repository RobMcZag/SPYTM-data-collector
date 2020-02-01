package com.robertozagni.SPYTM.data.collector;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVDailyQuote;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerie;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerieMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.mockito.Mockito.*;

@SpringBootTest
class SpytmDataCollectorApplicationTests {

	@MockBean
	RestTemplate mockTemplate;

	@Autowired
	CommandLineRunner commandLineRunner;

	@Test
	void contextLoads() {
	}

	@Test
	void commandLineRunner_bean_exists_and_calls_correct_template() throws Exception {
		AVTimeSerie emptyAVTimeSerie = makeEmptyTimeSerieData();
		when(mockTemplate.getForObject(anyString(), eq(AVTimeSerie.class)))
				.thenReturn(emptyAVTimeSerie);

		commandLineRunner.run("DAILY_ADJUSTED", "MSFT", "AAPL");

		verify(mockTemplate, times(2)).getForObject(anyString(), eq(AVTimeSerie.class));;
	}

	private AVTimeSerie makeEmptyTimeSerieData(){
		return new AVTimeSerie(makeTestTimeSerieMetadata(), new HashMap<String, AVDailyQuote>());
	}

	private AVTimeSerieMetadata makeTestTimeSerieMetadata() {
		return new AVTimeSerieMetadata(
				"Daily data",
				"APPL",
				"2020-09-27",
				"compact",
				"US/SomePlace");
	}

}
