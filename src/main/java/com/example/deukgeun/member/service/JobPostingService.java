package com.example.deukgeun.member.service;

import com.example.deukgeun.commom.entity.JobPosting;
import com.example.deukgeun.commom.request.SaveJobPostingRequest;

public interface JobPostingService {
    JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId);
}