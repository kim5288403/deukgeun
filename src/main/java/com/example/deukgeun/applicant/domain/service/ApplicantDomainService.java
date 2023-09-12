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
    void deleteMatchInfoById(Long id);
    Applicant findById(Long id);
    Page<Applicant> getByJobId(Long jobId, PageRequest pageRequest);
    boolean isAnnouncementMatchedByJobId(Long jobId);
    Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId);
    Applicant savePaymentInfo(PaymentInfoRequest request, LocalDateTime paidAt);
    Applicant saveMatchInfo(SaveMatchInfoRequest saveMatchInfoRequest, int status);
    void updatePaymentCancelInfoById(Long id, IamPortCancelResponse iamPortCancelResponse);
    void updateIsSelectedById(Long applicantId, int isSelected);
}
