package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}