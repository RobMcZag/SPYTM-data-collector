package com.robertozagni.SPYTM.data.collector;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Configures the "spring.datasource" as Primary and to be used for the collector repositories.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "collectorEntityManagerFactory",  // Default would be 'entityManagerFactory'
        transactionManagerRef = "collectorTransactionManager",      // Default would be 'transactionManager'
        basePackages = { "com.robertozagni.SPYTM.data.collector.repository" }
)
public class CollectorConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Primary
    @Bean(name = "collectorDatasourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties collectorDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Primary @FlywayDataSource
    @Bean(name = "collectorDatasource")
    public DataSource collectorDatasource(@Qualifier("collectorDatasourceProperties")
                                                      DataSourceProperties defaultDataSourceProperties) {
        return defaultDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "collectorEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("collectorDatasource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {

        return builder
                .dataSource(dataSource)
                .packages("com.robertozagni.SPYTM.data.collector")
                .build();
    }

    @Primary
    @Bean(name = "collectorTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("collectorEntityManagerFactory")
                                                                     EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
