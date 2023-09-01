package com.example.deukgeun.job.domain.service;

import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface JobDomainService {
    boolean existsByIdAndMemberId(Long id, Long memberId);
    Job findById(Long id);
    Page<Job> getListByKeyword(String keyword, PageRequest pageRequest);
    Page<Job> getListByMemberId(Long memberId, PageRequest pageRequest);
    Job save(SaveJobRequest saveJobRequest, Long memberId);
    Job updateIsActiveByJobId(int isActive, Long id);
}
