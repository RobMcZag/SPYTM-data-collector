package com.robertozagni.SPYTM.data.collector;

import com.robertozagni.SPYTM.data.collector.model.alphavantage.StockData;
import com.robertozagni.SPYTM.data.collector.model.alphavantage.TimeSerieData;
import com.robertozagni.SPYTM.data.collector.model.alphavantage.TimeSerieMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
		TimeSerieData emptyTimeSerieData = makeEmptyTimeSerieData();
		Mockito.when(mockTemplate.getForObject(anyString(), eq(TimeSerieData.class)))
				.thenReturn(emptyTimeSerieData);

		commandLineRunner.run("TIME_SERIES_DAILY_ADJUSTED", "MSFT", "AAPL");

		verify(mockTemplate, times(2)).getForObject(anyString(), eq(TimeSerieData.class));;
	}

	private TimeSerieData makeEmptyTimeSerieData(){
		return new TimeSerieData(new TimeSerieMetadata(), new HashMap<String, StockData>());
	}

}
