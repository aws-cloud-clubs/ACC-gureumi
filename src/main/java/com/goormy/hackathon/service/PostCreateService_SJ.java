package com.goormy.hackathon.service;

import com.amazonaws.HttpMethod;
import com.goormy.hackathon.dto.hashtag.HashtagResponseDto_SJ;
import com.goormy.hackathon.dto.post.PostRequestDto_SY;
import com.goormy.hackathon.dto.post.PostResponseDto_SJ;
import com.goormy.hackathon.dto.post.PostResponseDto_SY;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.repository.PostRepository_SY;
import com.goormy.hackathon.repository.UserRepository_SY;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCreateService_SJ {

    private final UserRepository_SY userRepositorySY;
    private final PostRepository_SY postRepositorySY;
    private final HashtagService_SY hashtagServiceSY;
    private final PostCacheService_SY postCacheServiceSY;
    private final PhotoService_SJ photoServiceSJ;


    @Transactional
    public PostResponseDto_SJ createPost_SJ(Long userId, PostRequestDto_SY postRequestDtoSY) {
        // 사용자 찾기
        var user = userRepositorySY.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found"));  // TODO: Custom exception

        // post 생성
        var post = Post.builder()
                .user(user)
                .content(postRequestDtoSY.content())
                .imageUrl(postRequestDtoSY.imageUrl())
                .star(postRequestDtoSY.star())
                .likeCount(0)
                .build();

        // hashtag 생성
        var postHashtags = hashtagServiceSY.getOrCreateHashtags(postRequestDtoSY.postHashtags());
        post.setPostHashtags(postHashtags);

        // DB 저장
        postRepositorySY.save(post);

        // redis 저장
        postCacheServiceSY.cache(post);

        String imageUrl = photoServiceSJ.getPreSignedUrl(post.getId(), post.getImageUrl(), HttpMethod.PUT);
        List<HashtagResponseDto_SJ> hashtags = post.getPostHashtags()
                .stream().map(HashtagResponseDto_SJ::toDto).toList();

        return PostResponseDto_SJ.toDtoFromPost(post, imageUrl, user, hashtags);
    }

}
