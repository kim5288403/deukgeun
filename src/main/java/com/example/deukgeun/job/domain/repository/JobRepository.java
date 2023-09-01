package com.example.deukgeun.job.domain.repository;

import com.example.deukgeun.job.domain.model.aggregate.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface JobRepository {
    boolean existsByIdAndMemberId(Long id, Long memberId);
    Optional<Job> findById(Long id);
    Page<Job> findByMemberId(Long memberId, PageRequest pageRequest);
    Page<Job> findByLikeKeyword(String keyword, PageRequest pageRequest);
    Job save(Job job);
}
