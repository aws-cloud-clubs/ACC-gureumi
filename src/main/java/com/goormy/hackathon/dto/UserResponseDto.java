package com.goormy.hackathon.dto;

import lombok.Builder;

@Builder
public class UserResponseDto {
    private Long userId;
    private String name;
}
