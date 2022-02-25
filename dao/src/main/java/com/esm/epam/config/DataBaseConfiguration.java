package com.esm.epam.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static com.esm.epam.util.ParameterAttribute.DATABASE_DRIVER;
import static com.esm.epam.util.ParameterAttribute.DATABASE_PASSWORD;
import static com.esm.epam.util.ParameterAttribute.DATABASE_URL;
import static com.esm.epam.util.ParameterAttribute.DATABASE_USERNAME;

@Configuration
@ComponentScan(basePackages = {"com.esm.epam.repository.impl"})
public class DataBaseConfiguration {
    @Bean
    @Qualifier(value = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(DATABASE_URL);
        dataSource.setUsername(DATABASE_USERNAME);
        dataSource.setPassword(DATABASE_PASSWORD);
        dataSource.setDriverClassName(DATABASE_DRIVER);
        return dataSource;
    }

}
