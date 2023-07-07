package com.example.deukgeun.member.service;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.member.request.SaveJobPostingRequest;

public interface JobPostingService {
    JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId);
}
