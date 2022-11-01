package com.example.cryptoapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@EnableRetry
@Slf4j
public class CryptoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoApiApplication.class, args);
    }

    @Bean
    public RestTemplateBuilder builder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public Executor taskExecutor() {
        log.info("Configuring task executor...");
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setKeepAliveSeconds(1);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("TaskExecutor");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
