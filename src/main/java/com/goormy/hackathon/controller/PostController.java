package com.goormy.hackathon.controller;

import com.goormy.hackathon.dto.post.PostCreateRequestDto;
import com.goormy.hackathon.dto.post.PostResponseDto;
import com.goormy.hackathon.redis.entity.PostRedis;
import com.goormy.hackathon.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @RequestHeader("userId") Long userId,
            @RequestBody PostCreateRequestDto request) {
        return ResponseEntity.ok(postService.createPost(userId, request));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("post_id") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable("post_id") Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }
}
