package com.example.deukgeun.applicant.application.service;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.dto.response.PaymentResponse;

public interface PaymentApplicationService {
    PaymentResponse.Info getPaymentInfo(Long id);
    void savePaymentInfo(PaymentInfoRequest request);
    void updatePaymentCancelInfoById(Long id, IamPortCancelResponse iamPortCancelResponse);
}
