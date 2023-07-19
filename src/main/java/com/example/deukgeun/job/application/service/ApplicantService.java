package com.example.deukgeun.job.application.service;

import com.example.deukgeun.job.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.job.application.dto.response.ApplicantResponse;
import com.example.deukgeun.job.domain.entity.Applicant;
import org.springframework.data.domain.Page;

public interface ApplicantService {
    Page<ApplicantResponse.ListResponse> getByJobPostingId(Long jobPostingId, int currentPage);
    void updateIsSelectedByApplicantId(Long applicantId, int isSelected);
    ApplicantResponse.ApplicantInfo getById(Long id);
    Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId);

}
