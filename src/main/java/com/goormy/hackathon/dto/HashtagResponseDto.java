package com.goormy.hackathon.dto;

import com.goormy.hackathon.entity.PostHashtag;
import lombok.Builder;

@Builder
public class HashtagResponseDto {
    private Long id;
    private String name;

    public static HashtagResponseDto toDto(PostHashtag hashtag) {
        return HashtagResponseDto.builder()
                .id(hashtag.getId())
                .name(hashtag.getHashtag().getName())
                .build();
    }
}
