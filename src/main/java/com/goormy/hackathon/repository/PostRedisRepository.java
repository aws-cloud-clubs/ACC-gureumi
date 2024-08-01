package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.PostRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(PostRedis postRedis) {
        redisTemplate.opsForList().leftPush(postRedis.getPostId().toString(), postRedis);
    }

    public void findById() {

    }

}