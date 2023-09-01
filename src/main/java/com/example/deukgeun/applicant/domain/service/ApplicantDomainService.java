package com.example.deukgeun.applicant.domain.service;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

public interface ApplicantDomainService {
    void cancel(Long id, IamPortCancelResponse iamPortCancelResponse);
    void deleteMatchInfoById(Long id);
    Applicant findById(Long id);
    Page<Applicant> getByJobId(Long jobId, PageRequest pageRequest);
    boolean isAnnouncementMatchedByJobId(Long jobId);
    Applicant matching(SaveMatchInfoRequest saveMatchInfoRequest, int status);
    Applicant payment(PaymentInfoRequest request, LocalDateTime paidAt);
    Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId);
    void updateIsSelectedById(Long applicantId, int isSelected);
}
