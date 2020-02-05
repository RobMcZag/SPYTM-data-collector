package com.robertozagni.SPYTM.data.collector.datalake;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.validation.constraints.NotNull;

@Component @ConfigurationProperties(prefix = "datalake")
@Getter @Setter @NoArgsConstructor
public class SnowflakeProperties implements BeanClassLoaderAware {

    public static final String JDBC_SNOWFLAKE_SCHEME = "jdbc:snowflake://";
    public static final String SNOWFLAKE_JDBC_DRIVER_NAME = "net.snowflake.client.jdbc.SnowflakeDriver";

    private ClassLoader classLoader;

    private @NotNull String url;    //  jdbc:snowflake://wr04231.eu-west-1.snowflakecomputing.com
    private String account;
    private String region;
    private @NotNull String username;   // testappuser
    private @NotNull String password; //: testAppUserAcc0unt
    private @NotNull String role; // SPYTMLAKE_USER_ROLE
    private String databaseName; // SPYTMLAKE_TEST
    private String schema;
    private String warehouse;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @NotNull
    private String determineDriverClassName() {
        return SNOWFLAKE_JDBC_DRIVER_NAME;
    }

    /**
     * Returns the url set in the bean, if it looks like a Snowflake one.
     * Otherwise builds it from account and region info.
     * @throws SnowflakeDataSourceCreationException if url is not a Snowflake url and account is not set.
     */
    @NotNull
    public String determineSnowflakeUrl() {
        if (isSnowflakeUrl()) {
            return url;
        }
        if (account != null) {
            String fullAccountName = account;
            if (region != null) {
                fullAccountName = fullAccountName + "." + region;
            }
            return JDBC_SNOWFLAKE_SCHEME + fullAccountName + ".snowflakecomputing.com/";
        }
        throw new SnowflakeDataSourceCreationException("Failed to determine a suitable URL for Snowflake connection.", this);
    }

    private boolean isSnowflakeUrl() {
        return url.startsWith(JDBC_SNOWFLAKE_SCHEME);
    }

    /**
     * Veriefies if the Snowflake driver is loadable
     * @return True if the driver is loadable, False otherwise.
     */
    public boolean snowflakeDriverIsLoadable() {
        try {
            ClassUtils.forName(SNOWFLAKE_JDBC_DRIVER_NAME, null);
            return true;
        }
        catch (UnsupportedClassVersionError ex) {
            // Driver library has been compiled with a later JDK, propagate error
            throw ex;
        }
        catch (Throwable ex) {
            return false;
        }
    }


    static class SnowflakeDataSourceCreationException extends RuntimeException {
        @Getter private SnowflakeProperties snowflakeProperties;
        public SnowflakeDataSourceCreationException(String message, SnowflakeProperties snowflakeProperties) {
            super(message);
            this.snowflakeProperties = snowflakeProperties;
        }
    }
}