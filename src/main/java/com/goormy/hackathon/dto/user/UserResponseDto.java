package com.goormy.hackathon.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDto {

    private Long userId;
    private String name;
}
