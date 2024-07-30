package com.goormy.hackathon.dto;

import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {
    private String content;
    private Integer star;
    private Long userId;

    public Post toEntity(User user) {
        return Post.builder()
                .content(content)
                .star(star)
                .user(user)
                .build();
    }

}
