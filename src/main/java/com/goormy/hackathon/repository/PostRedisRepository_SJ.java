package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.PostCache_SY;
import com.goormy.hackathon.redis.entity.PostRedis_SJ;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRedisRepository_SJ {
    private final RedisTemplate<String, Object> redisTemplate;

    // 포스트 삭제
    public void delete(Long postId) {
        String key = "post:" + postId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.delete(key);
        }
    }

    // 포스트 저장
    public void set(PostRedis_SJ postRedisSJ) {
        redisTemplate.opsForList().leftPush("post:" + postRedisSJ.getId(), postRedisSJ);
    }

    // 포스트 조회
    public PostRedis_SJ get(Long postId) {
        String key = "post:" + postId;
        return (PostRedis_SJ) redisTemplate.opsForValue().get(key);
    }
}
