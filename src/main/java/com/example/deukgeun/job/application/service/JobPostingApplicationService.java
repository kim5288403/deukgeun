package com.example.deukgeun.job.application.service;

import com.example.deukgeun.job.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.job.application.dto.response.JobPostingResponse;
import com.example.deukgeun.job.domain.model.aggregate.JobPosting;
import org.springframework.data.domain.Page;

public interface JobPostingApplicationService {
    boolean existsByIdAndMemberId(Long id, Long memberId);
    JobPosting findById(Long id);
    Page<JobPostingResponse.List> getListByKeyword(String keyword, int currentPage);
    Page<JobPostingResponse.List> getListByMemberId(Long memberId, int currentPage);
    JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId);
    void updateIsActiveByJobPostingId(int isActive, Long id);
}
