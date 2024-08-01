package com.goormy.hackathon.redis.entity;

import com.goormy.hackathon.common.util.LocalDateTimeConverter;
import com.goormy.hackathon.dto.hashtag.HashtagResponseDto;
import com.goormy.hackathon.dto.user.UserResponseDto;
import com.goormy.hackathon.entity.Post;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash(value = "post_", timeToLive = 60 * 60 * 24)
@Builder
@Getter
@Data
public class PostRedis implements Serializable {

    @Id
    private String postId;
    private String content;
    private Integer star;
    private int likeCount;
    private UserResponseDto user;
    private String imageName;

    private List<HashtagResponseDto> postHashtags = new ArrayList<>();
    private String createdAt;
}
