package com.robertozagni.SPYTM.data;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

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

	@Primary
	@Bean(name = "defaultDatasourceProperties")
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties defaultDatasourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "defaultDatasource")
	public DataSource localDbDatasource(@Qualifier("defaultDatasourceProperties") DataSourceProperties defaultDataSourceProperties) {
		return defaultDataSourceProperties
				.initializeDataSourceBuilder()
				.type(HikariDataSource.class)
				.build();
	}

}
