package com.example.deukgeun.job.application.service;

import com.example.deukgeun.job.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.job.application.dto.response.JobPostingResponse;
import com.example.deukgeun.job.domain.entity.JobPosting;
import org.springframework.data.domain.Page;

public interface JobPostingService {
    Page<JobPostingResponse.ListResponse> getListByKeyword(String keyword, int currentPage);
    JobPosting getById(Long id);
    boolean existsByIdAndMemberId(Long id, Long memberId);
    JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId);
    JobPosting updateIsActiveByJobPostingId(int isActive, Long jobPostingId);
    Page<JobPostingResponse.ListResponse> getByMemberId(Long memberId, Integer currentPage);

}
