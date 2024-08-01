package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.dto.hashtag.HashtagResponseDto;
import com.goormy.hackathon.dto.user.UserResponseDto;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class PostDetailResponseDto {

  private Long id;
  private UserResponseDto user;
  private String content;
  private long likeCount;
  private String imageUrl;
  private int str;

  private List<HashtagResponseDto> hashtags = new ArrayList<>();

  // created_at 추가

  private boolean isLike;
}
