package com.esm.epam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
@Configuration
public class HsqlConfiguration {
    @Bean
    public JdbcTemplate hsqlJdbcTemplate() {
        return new JdbcTemplate(hsqlDataSource());
    }

    @Bean
    public DataSource hsqlDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:schema.sql")
                .addScript("classpath:test_data.sql")
                .build();
    }

}
