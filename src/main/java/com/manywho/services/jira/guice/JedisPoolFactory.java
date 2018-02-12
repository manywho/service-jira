package com.manywho.services.jira.guice;

import com.google.common.base.Strings;
import com.google.inject.Provider;
import redis.clients.jedis.JedisPool;

public class JedisPoolFactory implements Provider<JedisPool> {
    @Override
    public JedisPool get() {
        String uri = System.getenv("REDIS_URL");
        if (Strings.isNullOrEmpty(uri)) {
            uri = "localhost:6379";
        }

        return new JedisPool(uri);
    }
}
