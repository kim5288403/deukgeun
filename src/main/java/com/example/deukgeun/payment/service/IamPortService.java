package com.example.deukgeun.payment.service;

import com.example.deukgeun.payment.request.CancelRequest;
import com.example.deukgeun.payment.response.IamPortCancelResponse;

public interface IamPortService {
    void checkCancelResponseCode(IamPortCancelResponse response) throws Exception;
    IamPortCancelResponse cancelIamPort(CancelRequest request);
    String getIamPortAuthToken(String iamPortApiKey, String iamPortApiSecret);
}
