package com.polafacebook.model.repositories;

import com.adapters.dto.ContextEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {

    private RedisTemplate<String, ContextEntity> redisTemplate;
    private ValueOperations<String, ContextEntity> valueOperations;

    private static final String KEY = "Context:";

    public RedisRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void save(ContextEntity redisContext, long timeout, TimeUnit timeUnit) {
        String fullName = KEY + redisContext.userId;
        valueOperations.set(fullName, redisContext);
        redisTemplate.expire(fullName, timeout, timeUnit);
    }

    public void save(ContextEntity redisContext) {
        String fullName = KEY + redisContext.userId;
        valueOperations.set(fullName, redisContext);
    }

    public ContextEntity get(String id){
        return valueOperations.get(KEY + id);
    }

    public void delete(String id) {
        redisTemplate.delete(KEY + id);
    }
}
