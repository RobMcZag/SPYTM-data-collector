package com.robertozagni.SPYTM.data.datalake.service;

import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SnowflakeStorageService {

    private final JdbcTemplate snowflakedb;

    @Autowired
    public SnowflakeStorageService(@Qualifier("SnowflakeBasicDataSource") SnowflakeBasicDataSource sfDatasource) {
        this.snowflakedb = new JdbcTemplate(sfDatasource);
    }

    public void save(TimeSerie timeSerie) {
        // TODO Implement TS save on SF
    }

    public void checkConnection() {
        snowflakedb.query("Select 1;", (rs, rowNum) -> null);   // Just check we can run a query
        log.info("Connection to Snowflake is working. :) ");
    }
}

