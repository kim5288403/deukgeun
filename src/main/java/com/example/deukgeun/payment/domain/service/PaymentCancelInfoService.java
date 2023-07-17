package com.example.deukgeun.payment.domain.service;

import com.example.deukgeun.payment.domain.entity.PaymentCancelInfo;
import com.example.deukgeun.payment.application.dto.response.IamPortCancelResponse;

public interface PaymentCancelInfoService {
    PaymentCancelInfo save(IamPortCancelResponse iamPortCancelResponse);
}
