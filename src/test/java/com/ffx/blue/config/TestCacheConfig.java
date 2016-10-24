package com.ffx.blue.config;

import com.ffx.blue.common.utils.ClassUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.ffx.blue.article.service", "com.ffx.blue.article.domain"})
@EnableAutoConfiguration
@EnableCaching
public class TestCacheConfig {
    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(ClassUtils.ARTICLES_COLLECTION_NAME);
    }
}
