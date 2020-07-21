package com.robertozagni.SPYTM.data.datalake;

import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SnowflakeConfig {

    /**
     * Creates an OPTIONAL Snowflake datasource.
     * @param snowflakeProperties the bean with the info to use to create the datasource.
     * @return null or a newly allocated Snowflake data source.
     *          The datasource is created if the property "active" is true in the Spring config
     *          and if the data provided in the config fields is correct.
     */
    @Bean(name = "SnowflakeBasicDataSource")
    public SnowflakeBasicDataSource snowflakeDatasource(SnowflakeProperties snowflakeProperties){
        if (snowflakeProperties.isActive()
            && snowflakeProperties.snowflakeDriverIsLoadable()) {
            try {
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
            } catch (SnowflakeProperties.SnowflakeDataSourceCreationException
                    | IllegalStateException ignoredButLogged) {
                log.error("Exception in initialising ACTIVE Snowflake DataSource. "
                        + ignoredButLogged.getMessage());
            }
        }
        snowflakeProperties.setActive(false);
        return null;    // we do not build a SF DS => we explicitly set as Inactive in the properties
    }
}
