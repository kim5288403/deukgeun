package com.example.deukgeun.applicant.application.service;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import org.springframework.data.domain.Page;

public interface ApplicantApplicationService {
    void deleteMatchInfoById(Long id);
    Applicant findById(Long id);
    Page<ApplicantResponse.List> getByJobId(Long jobId, int currentPage);
    void isAnnouncementMatchedByJobId(Long jobId);
    Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId);
    Applicant savePaymentInfo(PaymentInfoRequest request);
    Applicant saveMatchInfo(SaveMatchInfoRequest saveMatchInfoRequest, int status);
    void updatePaymentCancelInfoById(Long id, IamPortCancelResponse iamPortCancelResponse);
    void updateIsSelectedById(Long id, int isSelected);

}
