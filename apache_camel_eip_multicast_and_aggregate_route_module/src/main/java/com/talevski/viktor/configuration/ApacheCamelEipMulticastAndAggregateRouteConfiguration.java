package com.talevski.viktor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Configuration
public class ApacheCamelEipMulticastAndAggregateRouteConfiguration {

    @Bean
    public ExecutorService getExecutorService() {
        return newFixedThreadPool(10);
    }
}
