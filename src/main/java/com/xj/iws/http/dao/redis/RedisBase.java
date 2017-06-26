package com.xj.iws.http.dao.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/25.
 */
@Component
public class RedisBase {
    @Autowired
    private RedisTemplate redisTemplate;

    private RedisConnection connection;

    public RedisConnection getConnection(){
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                connection = redisConnection;
                return null;
            }
        });
        return this.connection;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public ValueOperations valueOps(){
        return redisTemplate.opsForValue();
    }

    public HashOperations hashOps(){
        return redisTemplate.opsForHash();
    }

    public ZSetOperations zSetOps(){
        return redisTemplate.opsForZSet();
    }

    public SetOperations setOps(){
        return redisTemplate.opsForSet();
    }

    public ListOperations listOps(){
        return redisTemplate.opsForList();
    }

    public BoundValueOperations bValueOps(String key){
        return redisTemplate.boundValueOps(key);
    }

    public BoundHashOperations bHashOps(String key){
        return redisTemplate.boundHashOps(key);
    }

    public BoundZSetOperations bZSetOps(String key){
        return redisTemplate.boundZSetOps(key);
    }

    public BoundListOperations bListOps(String key){
        return redisTemplate.boundListOps(key);
    }

    public RedisSerializer serializer(){ return redisTemplate.getStringSerializer(); }
}
