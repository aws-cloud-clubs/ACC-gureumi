package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.dto.hashtag.HashtagResponseDto;
import com.goormy.hackathon.dto.user.UserResponseDto;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.PostHashtag;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Builder
@Getter
public class PostRedisSaveDto implements Serializable {

  @Id
  private String postId;
  private String content;
  private Integer star;
  private int likeCount;
  private UserResponseDto user;
  private String imageName;
  private List<HashtagResponseDto> hashtags = new ArrayList<>();

  public static PostRedisSaveDto toDto(Post post, List<PostHashtag> hashtags) {
    return PostRedisSaveDto.builder()
        .postId(post.getId() + "")
        .content(post.getContent())
        .star(post.getStar())
        .likeCount(post.getLikeCount())
        .user(UserResponseDto.builder()
            .userId(post.getUser().getId())
            .name(post.getUser().getName())
            .build())
        .imageName(post.getImageName())
        .hashtags(hashtags.stream().map(HashtagResponseDto::toDto).toList())
        .build();
  }


}
