package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.common.util.LocalDateTimeConverter;
import com.goormy.hackathon.dto.hashtag.HashtagResponseDto;
import com.goormy.hackathon.dto.hashtag.PostHashtagRequestDto;
import com.goormy.hackathon.dto.user.UserResponseDto;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.PostHashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.redis.entity.PostRedis;
import java.util.List;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {

  private String content;
  private Integer star;
  private String imageName;
  private List<PostHashtagRequestDto> hashtags;

  public Post toEntity(User user) {
    return Post.builder()
        .content(content)
        .star(star)
        .user(user)
        .likeCount(0)
        .imageUrl(imageName)
        .build();
  }

  public PostRedis toRedisEntity(Post post) {
    return PostRedis.builder()
        .postId(post.getId().toString())
        .content(content)
        .star(star)
        .user(UserResponseDto.builder()
            .userId(post.getUser().getId())
            .name(post.getUser().getName())
            .build())
        .likeCount(0)
        .imageName(imageName)
        .postHashtags(post.getPostHashtags().stream().map(HashtagResponseDto::toDto).toList())
        .createdAt(LocalDateTimeConverter.convert(post.getCreatedAt()))
        .build();
  }
}
