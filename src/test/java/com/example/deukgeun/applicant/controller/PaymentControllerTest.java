package com.example.deukgeun.applicant.controller;

import com.example.deukgeun.applicant.application.controller.PaymentController;
import com.example.deukgeun.applicant.application.dto.request.CancelRequest;
import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.dto.response.PaymentResponse;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.application.service.MatchApplicationService;
import com.example.deukgeun.applicant.application.service.PaymentApplicationService;
import com.example.deukgeun.applicant.infrastructure.api.IamPortApiService;
import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;
    @Mock
    private PaymentApplicationService paymentApplicationService;
    @Mock
    private MatchApplicationService matchApplicationService;
    @Mock
    private IamPortApiService iamPortApiService;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenValidCancelRequest_whenCancel_thenReturnReturnOkResponse() throws Exception {
        // Given
        CancelRequest cancelRequest = mock(CancelRequest.class);
        IamPortCancelResponse iamPortCancelResponse = mock(IamPortCancelResponse.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("결제 취소 성공했습니다.", iamPortCancelResponse.getResponse());

        given(iamPortApiService.cancelIamPort(cancelRequest)).willReturn(iamPortCancelResponse);
        given(iamPortCancelResponse.getCode()).willReturn(0);

        // When
        ResponseEntity<?> responseEntity = paymentController.cancel(cancelRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(iamPortApiService, times(1)).cancelIamPort(cancelRequest);
        verify(matchApplicationService, times(1)).deleteMatchInfoById(cancelRequest.getId());
        verify(paymentApplicationService, times(1)).updatePaymentCancelInfoById(cancelRequest.getId(), iamPortCancelResponse);
    }

    @Test
    public void given_whenGetIamPortAuthToken_thenReturnReturnOkResponse() throws Exception {
        // Given
        String authToken = "authToken";

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", authToken);

        given(iamPortApiService.getIamPortAuthToken()).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = paymentController.getIamPortAuthToken();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(iamPortApiService, times(1)).getIamPortAuthToken();
    }

    @Test
    public void givenValidId_whenGetPaymentInfo_thenReturnReturnOkResponse() {
        // Given
        Long applicantId =1L;
        PaymentResponse.Info response = mock(PaymentResponse.Info.class);
        given(paymentApplicationService.getPaymentInfo(applicantId)).willReturn(response);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", response);

        // When
        ResponseEntity<?> responseEntity = paymentController.getPaymentInfo(applicantId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(paymentApplicationService, times(1)).getPaymentInfo(applicantId);
    }

    @Test
    public void givenValidImpUid_whenPayment_thenReturnReturnOkResponse() throws IamportResponseException, IOException {
        // Given
        String impUid = "imp12345";
        IamportResponse<Payment> expectedResponse = new IamportResponse<>();

        given(iamPortApiService.paymentByImpUid(impUid)).willReturn(expectedResponse);

        // When
        IamportResponse<Payment> result = paymentController.payment(impUid);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(iamPortApiService, times(1)).paymentByImpUid(impUid);
    }

    @Test
    public void givenValidPaymentRequest_whenPayment_thenReturnReturnOkResponse() {
        // Given
        PaymentInfoRequest request = mock(PaymentInfoRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("저장 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = paymentController.payment(request, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(paymentApplicationService, times(1)).savePaymentInfo(request);
    }
}
