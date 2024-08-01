package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Hashtag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

  Optional<Hashtag> findByName(String name);
}
