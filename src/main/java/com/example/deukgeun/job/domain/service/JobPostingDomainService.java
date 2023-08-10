package com.example.deukgeun.job.domain.service;

import com.example.deukgeun.job.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.job.domain.model.aggregate.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface JobPostingDomainService {
    boolean existsByIdAndMemberId(Long id, Long memberId);
    JobPosting findById(Long id);
    Page<JobPosting> getListByKeyword(String keyword, PageRequest pageRequest);
    Page<JobPosting> getListByMemberId(Long memberId, PageRequest pageRequest);
    JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId);
    JobPosting updateIsActiveByJobPostingId(int isActive, Long id);
}