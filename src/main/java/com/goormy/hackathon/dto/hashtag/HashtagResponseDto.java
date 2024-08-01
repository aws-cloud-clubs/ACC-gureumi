package com.goormy.hackathon.dto.hashtag;

import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.PostHashtag;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HashtagResponseDto {

    private Long id;
    private String name;
    private Hashtag.Type type;

    public static HashtagResponseDto toDto(PostHashtag hashtag) {
        return HashtagResponseDto.builder()
                .id(hashtag.getId())
                .name(hashtag.getHashtag().getName())
                .type(hashtag.getHashtag().getType())
                .build();
    }
}
