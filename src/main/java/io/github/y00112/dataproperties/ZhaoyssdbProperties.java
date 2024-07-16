package io.github.y00112.dataproperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("jdbc.properties")
public class ZhaoyssdbProperties {

    @Value("${zhaoyssdb.entityPackagePath}")
    private String entityPackagePath;

    @Value("${zhaoyssdb.datasource}")
    private String dataSourceType;

    @Value("${zhaoyssdb.url}")
    private  String jdbcUrl;

    @Value("${zhaoyssdb.username:root}")
    private  String jdbcUsername;

    @Value("${zhaoyssdb.password:123456}")
    private  String jdbcPassword;

    @Value("${zhaoyssdb.driverClassName}")
    private String driverClassName;

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getEntityPackagePath() {
        return entityPackagePath;
    }

    public void setEntityPackagePath(String entityPackagePath) {
        this.entityPackagePath = entityPackagePath;
    }
}
