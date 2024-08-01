package com.goormy.hackathon.service;

import com.amazonaws.HttpMethod;
import com.goormy.hackathon.dto.post.PostCreateRequestDto;
import com.goormy.hackathon.dto.post.PostResponseDto;
import com.goormy.hackathon.dto.post.PostRedisSaveDto;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.redis.entity.PostRedis;
import com.goormy.hackathon.repository.PostRedisRepository;
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
    private final PostRedisRepository postRedisRepository;
    private final PhotoService photoService;

    private final HashtagService hashtagService;

    // 포스트 생성
    @Transactional
    public PostResponseDto createPost(Long userId, PostCreateRequestDto request) {

        User user = userRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. "));

        // rds에 저장

        Post post = request.toEntity(user);

        postRepository.save(post);

        var postHashtags = hashtagService.getOrCreateHashtags(request.getHashtags());
        post.setPostHashtags(postHashtags);

        // redis 저장 Dto(photoUrl 포함)

        PostRedis redis = request.toRedisEntity(post);
        postRedisRepository.save(redis);
//    postRedisTemplate.opsForValue().set(post.getId() + "", saveRedisDto);

        // redis 반환 Dto(photoName 포함)
        String imageUrl = photoService.getPreSignedUrl(post.getId(), request.getImageName(),
                HttpMethod.PUT);

        return PostResponseDto.toDto(post, imageUrl);
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
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        System.out.println(postRedisTemplate.opsForValue()
                .get(postId + ""));

        Object redisData = postRedisTemplate.opsForValue()
                .get(postId + "");

        String imageUrl;
        if (redisData == null) {
            return getPostInRDB(postId);
        }

        return getPostInRedis(postId, (PostRedis) redisData);
    }

    // rdb에서 조회
    private PostResponseDto getPostInRDB(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 조회할 수 없습니다. "));
        String imageUrl = photoService.getPreSignedUrl(postId, post.getImageName(), HttpMethod.GET);

        postRedisTemplate.opsForValue().set(
                post.getId().toString(),
                PostRedisSaveDto.toDto(post, post.getPostHashtags()),
                Duration.ofHours(24));

        return PostResponseDto.toDto(post, imageUrl);
    }


    private PostResponseDto getPostInRedis(Long postId, PostRedis data) {
        String imageUrl = photoService.getPreSignedUrl(postId, data.getImageName(), HttpMethod.GET);
        return PostResponseDto.toDto(data, imageUrl);
    }
}
