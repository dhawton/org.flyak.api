package org.nzvirtual.api.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan
public class RedisConfig {
    @Value("${redis.server}")
    private String hostname;
    @Value("${redis.port}")
    private String port;

    @Bean(destroyMethod="shutdown")
    RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s", hostname, port));
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();

        config.put("routeMap", new CacheConfig(1 * 24 * 60 * 60 * 1000, 1 * 24 * 60 * 60 * 1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
