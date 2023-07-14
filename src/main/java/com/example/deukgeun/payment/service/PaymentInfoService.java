package com.example.deukgeun.payment.service;

import com.example.deukgeun.global.entity.PaymentInfo;
import com.example.deukgeun.payment.request.PaymentInfoRequest;

public interface PaymentInfoService {
    PaymentInfo save(PaymentInfoRequest request);
    PaymentInfo getPaymentInfoByApplicantId(Long applicantId);
    void deleteByImpUid(String impUid);
}
