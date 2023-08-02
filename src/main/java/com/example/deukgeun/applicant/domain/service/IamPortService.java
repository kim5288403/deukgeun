package com.example.deukgeun.applicant.domain.service;

import com.example.deukgeun.applicant.application.dto.request.CancelRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;

public interface IamPortService {
    void checkCancelResponseCode(IamPortCancelResponse response) throws Exception;
    IamPortCancelResponse cancelIamPort(CancelRequest request);
    String getIamPortAuthToken(String iamPortApiKey, String iamPortApiSecret);
}
