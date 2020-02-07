package com.robertozagni.SPYTM.data.collector;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
public class CollectorConfig {

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
