package com.goormy.hackathon.controller;

import com.goormy.hackathon.dto.PostCreateRequestDto;
import com.goormy.hackathon.dto.PostRedisResponseDto;
import com.goormy.hackathon.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostRedisResponseDto> createPost(
            @RequestBody PostCreateRequestDto request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("post_id") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<PostRedisResponseDto> getPost(
            @PathVariable("post_id") Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }
}
