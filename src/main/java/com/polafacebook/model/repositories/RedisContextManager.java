package com.polafacebook.model.repositories;

import com.adapters.dto.ContextEntity;
import com.adapters.dto.converters.ContextToRedisContextConverter;
import com.adapters.dto.converters.RedisContextToContextConverter;
import com.polafacebook.model.Context;
import org.springframework.stereotype.Repository;

@Repository
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
    public boolean saveContext(Context context) {
        redisRepository.save(outgoingConverter.produceRedisContext(context));
        return true;
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
    public boolean deleteContext(String id) {
        redisRepository.delete(id);
        return false;
    }

}
