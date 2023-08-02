package com.example.deukgeun.applicant.domain.service;

import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;

public interface PaymentCancelInfoService {
    PaymentCancelInfo save(IamPortCancelResponse iamPortCancelResponse);
}
