package com.wbohorquez.pruebaAlianza.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.ofDefaults("clientRateLimiter");
    }
}
