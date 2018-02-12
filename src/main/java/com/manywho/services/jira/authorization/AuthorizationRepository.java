package com.manywho.services.jira.authorization;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;

public class AuthorizationRepository {
    private final JedisPool jedisPool;

    @Inject
    public AuthorizationRepository(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void putTokenSecret(String token, String tokenSecret) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex("service:jira:token:" + token, 300, tokenSecret);
        }
    }
}
