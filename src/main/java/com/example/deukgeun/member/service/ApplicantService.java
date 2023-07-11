package com.example.deukgeun.member.service;

import com.example.deukgeun.member.response.ApplicantResponse;
import org.springframework.data.domain.Page;

public interface ApplicantService {
    Page<ApplicantResponse.ListResponse> getByJobPostingId(Long jobPostingId, int currentPage);
    void updateIsSelectedByApplicantId(Long applicantId, int isSelected);
}
