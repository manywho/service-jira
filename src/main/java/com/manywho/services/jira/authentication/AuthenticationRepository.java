package com.manywho.services.jira.authentication;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;

public class AuthenticationRepository {
    private final JedisPool jedisPool;

    @Inject
    public AuthenticationRepository(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public String getTokenSecret(String token) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get("service:jira:token:" + token);
        }
    }
}
