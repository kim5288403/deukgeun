package com.example.deukgeun.member.service;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.main.response.JobPostingResponse;
import com.example.deukgeun.member.request.SaveJobPostingRequest;
import org.springframework.data.domain.Page;

public interface JobPostingService {
    JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId);
    JobPosting updateIsActiveByJobPostingId(int isActive, Long jobPostingId);
    Page<JobPostingResponse.ListResponse> getByMemberId(Long memberId, Integer currentPage);

}
