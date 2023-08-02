package com.example.deukgeun.applicant.domain.service;

import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;

public interface PaymentInfoService {
    PaymentInfo save(PaymentInfoRequest request);
    PaymentInfo getPaymentInfoByApplicantId(Long applicantId);
    void deleteByImpUid(String impUid);
}
