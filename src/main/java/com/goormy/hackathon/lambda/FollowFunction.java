package com.goormy.hackathon.lambda;

import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.FollowRedisRepository;
import com.goormy.hackathon.repository.FollowRepository;
import com.goormy.hackathon.repository.HashtagRepository;
import com.goormy.hackathon.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Configuration
public class FollowFunction{

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private final FollowRedisRepository followRedisRepository;

    public FollowFunction(FollowRepository followRepository, UserRepository userRepository, HashtagRepository hashtagRepository, FollowRedisRepository followRedisRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.hashtagRepository = hashtagRepository;
        this.followRedisRepository = followRedisRepository;
    }

    @Bean
    public Consumer<Map<String, Object>> processFollow() {
        return messageBody -> {
            try {
                // userId와 hashtagId를 Number로 파싱하고 long으로 변환
                long userId = ((Number) messageBody.get("userId")).longValue();
                long hashtagId = ((Number) messageBody.get("hashtagId")).longValue();
                String action = (String) messageBody.get("action");

                User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다. userId: " + userId));
                Hashtag hashtag = hashtagRepository.findById(hashtagId).orElseThrow(() -> new RuntimeException("존재하지 않는 해시태그입니다. hashtagId: " + hashtagId));

                if ("follow".equals(action)) {
                    Follow follow = new Follow(user,hashtag);
                    followRepository.save(follow);
                    followRedisRepository.insertFollow(hashtagId, userId);
                    System.out.println("팔로우 성공: " + messageBody);
                } else if ("unfollow".equals(action)) {
                    Follow follow = followRepository.findByUserIdAndHashTagId(userId, hashtagId)
                            .orElseThrow(() -> new RuntimeException("존재하지 않는 팔로우입니다. userId: " + userId + " hashtagId: " + hashtagId));
                    followRepository.delete(follow);
                    followRedisRepository.removeFollow(hashtagId, userId);
                    System.out.println("팔로우 취소 성공: " + messageBody);
                } else {
                    System.out.println("존재하지 않는 action입니다 : " + action);
                }
            } catch (Exception e) {
                System.err.println("메시지 전송 실패: " + messageBody);
                e.printStackTrace();
            }
        };
    }

    // Redis 데이터를 RDBMS에 저장하고 Redis 비우기
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void migrateData() {
        // Redis에서 모든 팔로우 데이터 가져오기
        List<Follow> follows = followRedisRepository.getAllFollows();

        // RDBMS에 배치 저장
        followRepository.deleteAll();
        followRepository.saveAll(follows);

        // Redis 비우기

        System.out.println("Redis 데이터를 RDBMS로 옮기고 Redis를 초기화했습니다.");
    }


}
