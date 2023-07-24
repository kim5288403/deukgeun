package com.example.deukgeun.trainer.domain.repository;

import com.example.deukgeun.trainer.domain.model.entity.Post;

import java.util.Optional;

public interface PostRepository {
    void deleteById(Long id);
    boolean existsByTrainerId(Long trainerId);
    Optional<Post> findByTrainerId(Long id);
    Post save(Post post);
}
