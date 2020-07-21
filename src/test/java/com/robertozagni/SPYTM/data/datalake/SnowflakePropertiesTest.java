package com.robertozagni.SPYTM.data.datalake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SnowflakePropertiesTest {

    private SnowflakeProperties snowflakeProperties;

    @BeforeEach
    void setUp() {
        snowflakeProperties = new SnowflakeProperties();
    }

    @Test
    void determineSnowflakeUrl_throws_when_url_and_account_are_null() {
        snowflakeProperties.setUrl(null);
        snowflakeProperties.setAccount(null);
        assertThrows(
            SnowflakeProperties.SnowflakeDataSourceCreationException.class,
                () -> snowflakeProperties.determineSnowflakeUrl()
        );
    }
    @Test
    void determineSnowflakeUrl_does_not_throw_when_url_is_well_formed() {
        snowflakeProperties.setUrl(SnowflakeProperties.JDBC_SNOWFLAKE_SCHEME + "whatever");
        snowflakeProperties.setAccount(null);
        assertNotNull( snowflakeProperties.determineSnowflakeUrl() );
    }

    @Test
    void determineSnowflakeUrl_does_not_throw_when_account_is_not_null() {
        snowflakeProperties.setUrl(null);
        snowflakeProperties.setAccount("whatever");
        assertNotNull( snowflakeProperties.determineSnowflakeUrl() );
    }

    @Test
    void snowflakeDriverIsLoadable() {
    }
}