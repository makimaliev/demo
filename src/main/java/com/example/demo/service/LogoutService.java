package com.example.demo.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class LogoutService {

    private LoadingCache<String, String> logoutCache;

    public LogoutService() {
        super();
        logoutCache = CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
            public String load(String key) {
                return null;
            }
        });

    }

    public void loginSucceeded(String key) {
        logoutCache.invalidate(key);
    }

    public void logoutSucceeded(String key) {
        logoutCache.put(key, key);
    }

    public boolean isLoggedOut(String key) {
        if(logoutCache.size() == 0)
            return false;
        try {
            return logoutCache.get(key) != null;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
