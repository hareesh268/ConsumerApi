package com.optum.ds.service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EhCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EhCacheService.class.getCanonicalName());

    @Autowired
    CacheManager cacheManagerStgToken;

    EhCacheService() { }

    @Value("${ehCache_name}")
    private String ehCacheName ;


    public  void  addElement(String token, int time, String key){
        Element element = new Element(key,token);
        element.setTimeToLive(time);
        cacheManagerStgToken.getCache(ehCacheName).put(element);
        LOGGER.warn("EhCacheService::addElement:: Element added in EHCache with Key {}  Duration {}",
                key,cacheManagerStgToken.getCache(ehCacheName).get(key).getTimeToLive() );
    }

    public String getElement (String key){
        Cache cache = cacheManagerStgToken.getCache(ehCacheName);
        String token = null;
      if (null != cache.get(key)) {
          token =  (String) cache.get(key).getObjectValue();
          LOGGER.warn("EhCacheService::getElement:: fetching element for key {} expiry in {} ",key,cache.get(key).getExpirationTime() );
      }
      return token;
    }

}
