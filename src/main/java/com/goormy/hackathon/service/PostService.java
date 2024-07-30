package com.goormy.hackathon.service;

import com.goormy.hackathon.dto.PostCreateRequestDto;
import com.goormy.hackathon.dto.PostRedisResponseDto;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.PostRepository;
import com.goormy.hackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final RedisTemplate<String, Object> postRedisTemplate;
    private final UserRepository userRepository;

    // 포스트 생성
    @Transactional
    public PostRedisResponseDto createPost(PostCreateRequestDto request) {
        User user = userRepository.findById(request.getUserId()).get();
        Post post = request.toEntity(user);
        postRepository.save(post);
        PostRedisResponseDto redisDto = PostRedisResponseDto.toDto(post, post.getPostHashtags());
        postRedisTemplate.opsForValue().set(post.getId() + "", redisDto);
        System.out.println(redisDto);
        return (PostRedisResponseDto) postRedisTemplate.opsForValue().get(redisDto.getPostId());
    }


    // 포스트 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다. "));
        postRepository.delete(post);
        postRedisTemplate.delete(postId.toString());
    }

    // 포스트 단일 조회
    @Transactional
    public PostRedisResponseDto getPost(Long postId) {
        PostRedisResponseDto response = (PostRedisResponseDto) postRedisTemplate.opsForValue().get(postId +"");
        if (response == null) {
            Post post = postRepository.findById(postId).get();

            postRedisTemplate.opsForValue().set(
                    post.getId().toString(),
                    PostRedisResponseDto.toDto(post, post.getPostHashtags()),
                    Duration.ofHours(24));

            return PostRedisResponseDto.toDto(post, post.getPostHashtags());
        }
        return response;
    }
}
