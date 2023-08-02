package com.example.deukgeun.applicant.domain.service;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ApplicantDomainService {
    void deleteMatchInfoById(Long id);
    Applicant findById(Long id);
    Page<Applicant> getByJobPostingId(Long jobPostingId, PageRequest pageRequest);
    boolean isAnnouncementMatchedByJobPostingId(Long jobPostingId);
    Applicant matching(SaveMatchInfoRequest saveMatchInfoRequest, int status);
    Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId);
    void updateIsSelectedById(Long applicantId, int isSelected);
}
