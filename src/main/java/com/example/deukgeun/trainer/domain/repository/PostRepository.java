package com.example.deukgeun.trainer.domain.repository;

import com.example.deukgeun.trainer.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>{
  Optional<Post> findByTrainerId(Long trainerId);
  boolean existsByTrainerId(Long trainerId);
}
