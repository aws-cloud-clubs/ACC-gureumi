package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import java.util.List;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {

  private String content;
  private Integer star;
  private String imageName;
  private List<String> hashtags;

  public Post toEntity(User user) {
    return Post.builder()
        .content(content)
        .star(star)
        .user(user)
        .likeCount(0)
        .imageUrl(imageName)
        .build();
  }
}
