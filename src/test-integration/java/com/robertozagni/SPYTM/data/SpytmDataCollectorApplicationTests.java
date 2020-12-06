package com.robertozagni.SPYTM.data;

import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerie;
import com.robertozagni.SPYTM.data.collector.downloader.alphavantage.AVTimeSerieMetadata;
import com.robertozagni.SPYTM.data.collector.model.QuoteProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

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

		when( mockTemplate.getForObject(anyString(), eq(AVTimeSerie.class)) )
			  .thenReturn(makeEmptyTimeSerieData());

		commandLineRunner.run("DAILY_ADJUSTED", "MSFT", "AAPL", QuoteProvider.APLPHA_VANTAGE.name());

		verify(mockTemplate, times(2)).getForObject(anyString(), eq(AVTimeSerie.class));
	}

	private AVTimeSerie makeEmptyTimeSerieData(){
		return AVTimeSerie.builder().metadata(makeTestTimeSerieMetadata()).build();
	}

	private AVTimeSerieMetadata makeTestTimeSerieMetadata() {
		return new AVTimeSerieMetadata(
				"TEST Daily data",
				"TEST",
				"2020-09-27",
				"compact",
				"US/SomePlace");
	}

}
