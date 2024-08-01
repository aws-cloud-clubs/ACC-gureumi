package com.goormy.hackathon.repository;

import com.goormy.hackathon.dto.post.PostRedisResponseDto;
import org.springframework.data.repository.CrudRepository;

public interface PostRedisRepository extends CrudRepository<PostRedisResponseDto, String> {

}
