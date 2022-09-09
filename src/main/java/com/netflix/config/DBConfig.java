package com.netflix.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    @Value("${jdbcUrl}")
    private String jdbcUrl;
    @Value("${dbUsername}")
    private String username;
    @Value("${dbPassword}")
    private String password;

    @Bean(destroyMethod = "close")
    @Primary
    public DataSource getDataSource(){
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(jdbcUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return basicDataSource;
    }

}

