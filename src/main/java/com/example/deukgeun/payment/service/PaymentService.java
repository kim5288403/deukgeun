package com.example.deukgeun.payment.service;

import com.example.deukgeun.global.entity.PaymentInfo;
import com.example.deukgeun.payment.request.PaymentInfoRequest;

public interface PaymentService {
    PaymentInfo save(PaymentInfoRequest request);
}
