package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>{
  Optional<Post> findByTrainerId(Long trainerId);
  boolean existsByTrainerId(Long trainerId);
}
