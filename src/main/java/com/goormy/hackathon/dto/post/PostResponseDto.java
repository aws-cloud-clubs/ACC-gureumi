package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.dto.hashtag.HashtagResponseDto;
import com.goormy.hackathon.dto.user.UserResponseDto;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.redis.entity.PostRedis;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostResponseDto {

  private Long postId;
  private String content;
  private Integer star;
  private int likeCount;
  private UserResponseDto user;
  private String imageUrl;
  private List<HashtagResponseDto> postHashtags = Collections.emptyList();

  public static <T> PostResponseDto toDto(T post, String imageUrl) {
    if (post instanceof Post data) {
      return PostResponseDto.builder()
          .postId(Long.parseLong(data.getId().toString()))
          .content(data.getContent())
          .star(data.getStar())
          .likeCount(data.getLikeCount())
          .user(UserResponseDto.builder()
              .userId(data.getUser().getId())
              .name(data.getUser().getName())
              .build())
          .imageUrl(imageUrl)
          .postHashtags(data.getPostHashtags().stream().map(HashtagResponseDto::toDto).toList())
          .build();
    } else {
      PostRedis data = (PostRedis) post;
      return PostResponseDto.builder()
          .postId(Long.parseLong(data.getPostId().toString()))
          .content(data.getContent())
          .star(data.getStar())
          .likeCount(data.getLikeCount())
          .user(UserResponseDto.builder()
              .userId(data.getUser().getUserId())
              .name(data.getUser().getName())
              .build())
          .imageUrl(imageUrl)
          .postHashtags(data.getPostHashtags())
          .build();
    }
  }

  public void updatePhoto(String imageUrl) {
    this.imageUrl = imageUrl;
  }

}
