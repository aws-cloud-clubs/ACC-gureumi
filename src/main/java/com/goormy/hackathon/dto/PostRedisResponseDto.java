package com.goormy.hackathon.dto;

import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.PostHashtag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash(value = "post_", timeToLive = 60 * 60 * 24)
@Builder
@Getter
@Setter
public class PostRedisResponseDto implements Serializable {
    @Id
    private String postId;
    private String content;
    private Integer star;
    private String image;
    private int likeCount;
    private UserResponseDto user;
    private List<HashtagResponseDto> hashtags = new ArrayList<>();

    public static PostRedisResponseDto toDto(Post post, List<PostHashtag> hashtags) {
        return PostRedisResponseDto.builder()
                .postId(post.getId()+"")
                .content(post.getContent())
                .star(post.getStar())
                .image(post.getImageUrl())
                .likeCount(post.getLikeCount())
                .user(UserResponseDto.builder()
                        .userId(post.getUser().getId())
                        .name(post.getUser().getName())
                        .build())
                .hashtags(hashtags.stream().map(HashtagResponseDto::toDto).toList())
                .build();
    }
}
