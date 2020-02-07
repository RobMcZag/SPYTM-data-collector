package com.robertozagni.SPYTM.data.collector;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
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

/* TO have snake_case names for tables and columns in the DB the following, as by default, must be used by the EMF
        Map<String, Object> properties = factory.getJpaPropertyMap();
        properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
*/
    @Primary
    @Bean(name = "collectorEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("collectorDatasource")
                                                                                   DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.robertozagni.SPYTM.data.collector");
        factory.setDataSource(dataSource);
        return factory;
    }

    @Primary
    @Bean(name = "collectorTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("collectorEntityManagerFactory")
                                                                     EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
