package com.example.deukgeun.applicant.application.service;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import org.springframework.data.domain.Page;

public interface ApplicantApplicationService {
    void deleteMatchInfoById(Long id);
    ApplicantResponse.ApplicantInfo findById(Long id);
    Page<ApplicantResponse.ListResponse> getByJobPostingId(Long jobPostingId, int currentPage);
    void isAnnouncementMatchedByJobPostingId(Long jobPostingId);
    Applicant matching(SaveMatchInfoRequest saveMatchInfoRequest);
    Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId);
    void updateIsSelectedById(Long applicantId, int isSelected);

}
