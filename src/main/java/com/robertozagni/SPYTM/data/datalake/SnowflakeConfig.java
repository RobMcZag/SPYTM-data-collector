package com.robertozagni.SPYTM.data.datalake;

import com.zaxxer.hikari.HikariDataSource;
import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SnowflakeConfig {

    /**
     * Creates a Snowflake specific datasource from the properties configured in Spring.
     * @param snowflakeProperties the properties container bean with the info to use to create the datasource.
     * @return a newly allocated Snowflake data source
     * @throws SnowflakeProperties.SnowflakeDataSourceCreationException if the datasource can not be created
     */
    @Bean(name = "SnowflakeBasicDataSource")
    public SnowflakeBasicDataSource snowflakeDatasource(SnowflakeProperties snowflakeProperties){
        if (snowflakeProperties.snowflakeDriverIsLoadable()) {
            SnowflakeBasicDataSource dataSource = new SnowflakeBasicDataSource();
            dataSource.setUrl(snowflakeProperties.determineSnowflakeUrl());
            dataSource.setUser(snowflakeProperties.getUsername());
            dataSource.setPassword(snowflakeProperties.getPassword());
            dataSource.setRole(snowflakeProperties.getRole());
            if (snowflakeProperties.getDatabaseName() != null) {
                dataSource.setDatabaseName(snowflakeProperties.getDatabaseName());
            }
            if (snowflakeProperties.getWarehouse() != null) {
                dataSource.setWarehouse(snowflakeProperties.getWarehouse());
            }
            if (snowflakeProperties.getSchema() != null) {
                dataSource.setSchema(snowflakeProperties.getSchema());
            }
            return dataSource;
        }
        throw new SnowflakeProperties.SnowflakeDataSourceCreationException
                ("The Snowflake driver is not loadable.", snowflakeProperties);

    }
    @Bean(name = "snowflakeHikariDataSource")
    public HikariDataSource snowflakeHikariDatasource(SnowflakeProperties snowflakeProperties){
        return DataSourceBuilder.create(snowflakeProperties.getClassLoader())
				.type(HikariDataSource.class)
                .driverClassName(SnowflakeProperties.SNOWFLAKE_JDBC_DRIVER_NAME)
                .url(snowflakeProperties.determineSnowflakeUrl())
                .username(snowflakeProperties.getUsername())
                .password(snowflakeProperties.getPassword())
                .build();
    }

}
