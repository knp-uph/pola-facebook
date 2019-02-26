package com.adapters.outgoing.redis;

import com.adapters.outgoing.redis.converters.ContextEntity;
import com.adapters.outgoing.redis.converters.ContextToRedisContextConverter;
import com.adapters.outgoing.redis.converters.RedisContextToContextConverter;
import com.domain.ports.outgoing.context.Context;
import com.domain.ports.outgoing.context.ContextManager;

public class RedisContextManager implements ContextManager {

    private final RedisRepository redisRepository;
    private final RedisContextToContextConverter incomingConverter;
    private final ContextToRedisContextConverter outgoingConverter;

    public RedisContextManager(RedisRepository redisRepository, RedisContextToContextConverter incomingConverter, ContextToRedisContextConverter outgoingConverter) {
        this.redisRepository = redisRepository;
        this.incomingConverter = incomingConverter;
        this.outgoingConverter = outgoingConverter;
    }

    @Override
    public void saveContext(Context context) {
        redisRepository.save(outgoingConverter.produceRedisContext(context));
    }

    @Override
    public Context getContext(String id) {
        ContextEntity contextEntity = redisRepository.get(id);
        if (contextEntity != null) {
            return incomingConverter.produceContext(contextEntity);
        }
        return null;
    }

    @Override
    public Context getOrCreateContext(String id) {
        Context c = getContext(id);
        if(c != null){
            return c;
        }
        c = new Context(id);
        saveContext(c);
        return c;
    }

    @Override
    public void deleteContext(String id) {
        redisRepository.delete(id);
    }

}
