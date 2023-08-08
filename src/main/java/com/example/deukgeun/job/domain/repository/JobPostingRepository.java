package com.example.deukgeun.job.domain.repository;

import com.example.deukgeun.job.domain.model.aggregate.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface JobPostingRepository {
    boolean existsByIdAndMemberId(Long id, Long memberId);
    Optional<JobPosting> findById(Long id);
    Page<JobPosting> findByMemberId(Long memberId, PageRequest pageRequest);
    Page<JobPosting> findByLikeKeyword(String keyword, PageRequest pageRequest);
    JobPosting save(JobPosting jobPosting);
}
