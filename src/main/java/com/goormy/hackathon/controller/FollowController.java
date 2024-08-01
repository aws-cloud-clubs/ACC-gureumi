package com.goormy.hackathon.controller;

import com.goormy.hackathon.dto.HashtagDto;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.service.FollowService;
import com.goormy.hackathon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/followings")
public class FollowController {

    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<HashtagDto> getFollowedHashtags(@RequestHeader("userId") Long userId) {
        User user = userService.findById(userId);

        List<Hashtag> hashtags = followService.getFollowedHashtags(user);

        return hashtags.stream()
                .map(hashtag -> new HashtagDto(hashtag.getId(), hashtag.getName(), hashtag.getType().toString()))
                .collect(Collectors.toList());
    }
}
