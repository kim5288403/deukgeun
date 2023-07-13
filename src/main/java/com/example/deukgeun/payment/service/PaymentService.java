package com.example.deukgeun.payment.service;

import com.example.deukgeun.global.entity.PaymentInfo;
import com.example.deukgeun.payment.request.CancelRequest;
import com.example.deukgeun.payment.request.PaymentInfoRequest;
import com.example.deukgeun.payment.response.IamPortCancelResponse;

public interface PaymentService {
    PaymentInfo save(PaymentInfoRequest request);
    PaymentInfo getPaymentInfoByApplicantId(Long applicantId);
    String getIamPortAuthToken(String iamPortApiKey, String iamPortApiSecret);
    IamPortCancelResponse cancelIamPort(CancelRequest request);
}
