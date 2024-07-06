package org.example.Long2Short.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RBloomFilterConfiguration
 * @Description Redisson布隆过滤器配置
 * @Author CoderXshuai
 * @CreateTime 2024/7/7 0:07
 * @Version v1.0
 */
@Configuration
public class RBloomFilterConfiguration {
    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("userRegisterCachePenetrationBloomFilter");
        bloomFilter.tryInit(1000000, 0.03);
        return bloomFilter;
    }
}
