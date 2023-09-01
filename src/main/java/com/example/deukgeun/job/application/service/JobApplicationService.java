package com.example.deukgeun.job.application.service;

import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.application.dto.response.JobResponse;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import org.springframework.data.domain.Page;

public interface JobApplicationService {
    boolean existsByIdAndMemberId(Long id, Long memberId);
    Job findById(Long id);
    Page<JobResponse.List> getListByKeyword(String keyword, int currentPage);
    Page<JobResponse.List> getListByMemberId(Long memberId, int currentPage);
    Job save(SaveJobRequest saveJobRequest, Long memberId);
    void updateIsActiveByJobId(int isActive, Long id);
}
