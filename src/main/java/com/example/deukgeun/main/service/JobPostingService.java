package com.example.deukgeun.main.service;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.main.response.JobPostingResponse;
import org.springframework.data.domain.Page;

public interface JobPostingService {
    Page<JobPostingResponse.ListResponse> getList(String keyword, int currentPage);
    JobPosting getById(Long id);
}
