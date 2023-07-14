package com.example.deukgeun.payment.service;

import com.example.deukgeun.global.entity.PaymentCancelInfo;
import com.example.deukgeun.payment.request.CancelRequest;
import com.example.deukgeun.payment.response.IamPortCancelResponse;

public interface PaymentCancelInfoService {
    PaymentCancelInfo save(IamPortCancelResponse iamPortCancelResponse);
}
