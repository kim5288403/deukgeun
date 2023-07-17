package com.example.deukgeun.payment.domain.service;

import com.example.deukgeun.payment.domain.entity.PaymentInfo;
import com.example.deukgeun.payment.application.dto.request.PaymentInfoRequest;

public interface PaymentInfoService {
    PaymentInfo save(PaymentInfoRequest request);
    PaymentInfo getPaymentInfoByApplicantId(Long applicantId);
    void deleteByImpUid(String impUid);
}
