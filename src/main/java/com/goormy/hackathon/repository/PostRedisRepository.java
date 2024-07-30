package com.goormy.hackathon.repository;

import com.goormy.hackathon.dto.PostRedisResponseDto;
import org.springframework.data.repository.CrudRepository;

public interface PostRedisRepository extends CrudRepository<PostRedisResponseDto, String> {
}
