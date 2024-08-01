package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.dto.hashtag.HashtagResponseDto;
import com.goormy.hackathon.dto.user.UserResponseDto;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.PostHashtag;
import lombok.Builder;
import lombok.Data;
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
@Data
public class PostRedisResponseDto implements Serializable {

  @Id
  private Long postId;
  private String content;
  private Integer star;
  private int likeCount;
  private UserResponseDto user;
  private String imageUrl;
  private List<HashtagResponseDto> hashtags = new ArrayList<>();

  public static PostRedisResponseDto toDto(Post post, List<PostHashtag> hashtags, String imageUrl) {
    return PostRedisResponseDto.builder()
        .postId(post.getId())
        .content(post.getContent())
        .star(post.getStar())
        .likeCount(post.getLikeCount())
        .user(UserResponseDto.builder()
            .userId(post.getUser().getId())
            .name(post.getUser().getName())
            .build())
        .imageUrl(imageUrl)
        .hashtags(hashtags.stream().map(HashtagResponseDto::toDto).toList())
        .build();
  }

  public static PostRedisResponseDto fromSavedDto(PostRedisSaveDto data, String imageUrl) {
    return PostRedisResponseDto.builder()
        .postId(Long.parseLong(data.getPostId()))
        .content(data.getContent())
        .star(data.getStar())
        .likeCount(data.getLikeCount())
        .user(UserResponseDto.builder()
            .userId(data.getUser().getUserId())
            .name(data.getUser().getName())
            .build())
        .imageUrl(imageUrl)
        .hashtags(data.getHashtags())
        .build();
  }

  public void updatePhoto(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
