package com.forsythe.reviewservice;

import io.vertx.core.Vertx;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfiguration {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5433/flowable")
                .username("flowable")
                .password("flowable")
                .build();
    }

    @Bean
    public Vertx vertx() {
        return Vertx.vertx();
    }
}
