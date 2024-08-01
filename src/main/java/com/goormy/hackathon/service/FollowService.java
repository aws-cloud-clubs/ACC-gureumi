package com.goormy.hackathon.service;

import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.FollowCountRedisRepository;
import com.goormy.hackathon.repository.FollowRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final FollowCountRedisRepository followCountRedisRepository;

    // 유저가 팔로우하고 있는 해시태그 목록 조회
    public List<Hashtag> getFollowedHashtags(User user) {
        return followRepository.findHashtagsByUser(user);
    }

    // 팔로우, 언팔로우 캐시 부분
    @Transactional
    public void followHashtag(Long hashtagId) {
        Integer currentCount = followCountRedisRepository.getFollowCount(hashtagId);
        if (currentCount == null) {
            followCountRedisRepository.setFollowCount(hashtagId, 1); // 처음 팔로우인 경우 초기화
        } else {
            followCountRedisRepository.incrementFollowCount(hashtagId); // 기존 팔로우 수 증가
        }
    }

    @Transactional
    public void unfollowHashtag(Long hashtagId) {
        Integer currentCount = followCountRedisRepository.getFollowCount(hashtagId);
        if (currentCount != null && currentCount > 0) {
            followCountRedisRepository.decrementFollowCount(hashtagId); // 팔로우 수 감소
        }
    }
}
