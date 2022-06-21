package com.optum.ds.config;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${ehCache_name}")
    private String ehCacheName;
    @Value("${ehCache_heapSize}")
    private int heapSize;


    public  CacheManager ehCacheManager(net.sf.ehcache.config.Configuration config ) {
        return new CacheManager(config);
    }

    @Bean
    public  CacheManager  getConfig(){
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName(ehCacheName);
        cacheConfiguration.setMaxEntriesLocalHeap(heapSize);
        cacheConfiguration.setEternal(false);
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.dynamicConfig(true);
        config.addCache(cacheConfiguration);
        return  ehCacheManager(config);
    }
}
