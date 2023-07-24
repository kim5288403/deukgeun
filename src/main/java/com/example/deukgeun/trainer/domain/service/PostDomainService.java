package com.example.deukgeun.trainer.domain.service;

import com.example.deukgeun.trainer.domain.model.entity.Post;

import javax.persistence.EntityNotFoundException;

public interface PostDomainService {
    void deleteById(Long id);
    boolean existsByTrainerId(Long trainerId);
    Post findByTrainerId(Long id) throws EntityNotFoundException;
    Post save(String html, Long trainerId);
    Post update(Post post);
}
