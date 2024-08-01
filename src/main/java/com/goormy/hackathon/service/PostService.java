package com.goormy.hackathon.service;

import com.amazonaws.HttpMethod;
import com.goormy.hackathon.dto.post.PostCreateRequestDto;
import com.goormy.hackathon.dto.post.PostRedisResponseDto;
import com.goormy.hackathon.dto.post.PostRedisSaveDto;
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
  private final PhotoService photoService;

  // 포스트 생성
  @Transactional
  public PostRedisResponseDto createPost(Long userId, PostCreateRequestDto request) {

    User user = userRepository.findById(userId).
        orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. "));

    Post post = request.toEntity(user);

    // rds에 저장
    postRepository.save(post);

    // redis 반환 Dto
    String imageUrl = photoService.getPreSignedUrl(post.getId(), request.getImageName(),
        HttpMethod.POST);
    PostRedisResponseDto redisDto = PostRedisResponseDto.toDto(post, post.getPostHashtags(),
        imageUrl);

    // redis 저장 Dto
    PostRedisSaveDto saveRedisDto = PostRedisSaveDto.toDto(post, post.getPostHashtags());

    postRedisTemplate.opsForValue().set(post.getId() + "", saveRedisDto);

    System.out.println(redisDto);
//    return (PostRedisResponseDto) postRedisTemplate.opsForValue().get(redisDto.getPostId());
    return redisDto;
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
  public PostRedisResponseDto getPost(Long postId) {
    System.out.println(postRedisTemplate.opsForValue()
        .get(postId + ""));

    PostRedisResponseDto response = (PostRedisResponseDto) postRedisTemplate.opsForValue()
        .get(postId + "");

    String imageUrl;
    if (response == null) {
      Post post = postRepository.findById(postId)
          .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 조회할 수 없습니다. "));
      imageUrl = photoService.getPreSignedUrl(postId, post.getImageName(), HttpMethod.GET);

      postRedisTemplate.opsForValue().set(
          post.getId().toString(),
          PostRedisSaveDto.toDto(post, post.getPostHashtags()),
          Duration.ofHours(24));

      return PostRedisResponseDto.toDto(post, post.getPostHashtags(), imageUrl);
    }

    imageUrl = photoService.getPreSignedUrl(postId, response.getImageUrl(), HttpMethod.GET);
    response.updatePhoto(imageUrl);
    return response;
  }
}
