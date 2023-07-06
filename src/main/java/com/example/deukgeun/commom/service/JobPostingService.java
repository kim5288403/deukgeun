package com.example.deukgeun.commom.service;

import com.example.deukgeun.commom.response.JobPostingResponse;
import org.springframework.data.domain.Page;

public interface JobPostingService {
    Page<JobPostingResponse.ListResponse> getList(String keyword, int currentPage);
}
