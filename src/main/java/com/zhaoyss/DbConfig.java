package com.zhaoyss;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zhaoyss.dataproperties.ZhaoyssdbProperties;
import com.zhaoyss.orm.ZhaoyssdbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@EnableTransactionManagement
public class DbConfig {


    @Autowired
    private ZhaoyssdbProperties zhaoyssdbProperties;

    @Primary
    @Bean
    DataSource createDataSource() {
        switch (zhaoyssdbProperties.getDataSourceType().toLowerCase()) {
            case "hsqldb":
                return createHsqldbDataSource();
            case "h2":
                return createH2DataSource();
            case "mysql":
                return createMysqlDataSource();
            case "sqlite":
                return createSQLiteDataSource();
            default:
                throw new IllegalArgumentException("Unknown datasource type: " + zhaoyssdbProperties.getDataSourceType());
        }
    }

    DataSource createHsqldbDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(zhaoyssdbProperties.getDriverClassName());
        config.setJdbcUrl(zhaoyssdbProperties.getJdbcUrl());
        config.setUsername(config.getUsername());
        config.setPassword(config.getPassword());
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    DataSource createH2DataSource(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(zhaoyssdbProperties.getDriverClassName());
        config.setJdbcUrl(zhaoyssdbProperties.getJdbcUrl());
        config.setUsername(config.getUsername());
        config.setPassword(config.getPassword());
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5000");
        config.addDataSourceProperty("idleTimeout", "60000");
        return new HikariDataSource(config);
    }

    DataSource createSQLiteDataSource(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(zhaoyssdbProperties.getDriverClassName());
        config.setJdbcUrl(zhaoyssdbProperties.getJdbcUrl());
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    DataSource createMysqlDataSource(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(zhaoyssdbProperties.getDriverClassName());
        config.setJdbcUrl(zhaoyssdbProperties.getJdbcUrl());
        config.setUsername(zhaoyssdbProperties.getJdbcUsername());
        config.setPassword(zhaoyssdbProperties.getJdbcPassword());
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean
    ZhaoyssdbTemplate createZhaoyssdbTemplate(@Autowired JdbcTemplate jdbcTemplate){
        return new ZhaoyssdbTemplate(jdbcTemplate, zhaoyssdbProperties.getEntityPackagePath());
    }

}