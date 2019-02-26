package com.adapters.outgoing.redis;

import com.adapters.outgoing.redis.converters.*;
import com.domain.ports.outgoing.context.Context;
import com.domain.ports.outgoing.context.ContextManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RedisConfiguration {

    @Bean
    JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(30);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnFactory(JedisPoolConfig poolConfig) {
        try {
            URI redistogoUri = new URI(System.getenv("REDIS_URL"));
            JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory(poolConfig);

            jedisConnFactory.setUsePool(true);
            jedisConnFactory.setHostName(redistogoUri.getHost());
            jedisConnFactory.setPort(redistogoUri.getPort());
            jedisConnFactory.setTimeout(Protocol.DEFAULT_TIMEOUT);
            try {
                jedisConnFactory.setPassword(redistogoUri.getUserInfo().split(":", 2)[1]);
            } catch (NullPointerException exc) {
            }

            return jedisConnFactory;

        } catch (URISyntaxException e) {
            throw new IllegalStateException("Redis connection URI is invalid!", e);
        }
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public Jackson2JsonRedisSerializer<ContextEntity> jacksonJsonRedisJsonSerializer() {
        Jackson2JsonRedisSerializer<ContextEntity> jacksonJsonRedisJsonSerializer = new Jackson2JsonRedisSerializer<>(ContextEntity.class);
        return jacksonJsonRedisJsonSerializer;
    }

    @Bean
    public RedisTemplate<String, Context> redisTemplate(JedisConnectionFactory jedisConnFactory) {
        RedisTemplate<String, Context> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(jacksonJsonRedisJsonSerializer());
        return redisTemplate;
    }

    @Bean
    public ContextManager redisContextManager(RedisRepository redisRepository, RedisContextToContextConverter incomingConverter, ContextToRedisContextConverter outgoingConverter) {
        return new RedisContextManager(redisRepository, incomingConverter, outgoingConverter);
    }

    @Bean
    RedisRepository redisRepository(RedisTemplate redisTemplate) {
        return new RedisRepository(redisTemplate);
    }

    @Bean
    public RedisContextToContextConverter redisContextToContextConverter(UrlAttachmentEntityToUrlAttachmentConverter attachmentConverter) {
        return new RedisContextToContextConverter(attachmentConverter);
    }

    @Bean
    public ContextToRedisContextConverter redisContextConverter(UrlAttachmentToUrlAttachmentEntityConverter attachmentConverter) {
        return new ContextToRedisContextConverter(attachmentConverter);
    }

    @Bean
    public UrlAttachmentEntityToUrlAttachmentConverter urlAttachmentEntityToUrlAttachmentConverter() {
        return new UrlAttachmentEntityToUrlAttachmentConverter();
    }

    @Bean
    public UrlAttachmentToUrlAttachmentEntityConverter urlAttachmentToUrlAttachmentEntityConverter() {
        return new UrlAttachmentToUrlAttachmentEntityConverter();
    }

}
