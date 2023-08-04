package com.example.deukgeun.applicant.controller;

import com.example.deukgeun.applicant.application.controller.PaymentController;
import com.example.deukgeun.applicant.application.dto.request.CancelRequest;
import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.service.implement.ApplicantApplicationServiceImpl;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.PaymentCancelInfoService;
import com.example.deukgeun.applicant.infrastructure.persistence.api.IamPortApiService;
import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
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
    private PaymentCancelInfoService paymentCancelInfoService;
    @Mock
    private ApplicantApplicationServiceImpl applicantApplicationService;
    @Mock
    private IamPortApiService iamPortApiService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private IamportClient iamportClient;
    @Mock
    private HttpSession session;

    @Test
    public void givenImpUid_whenPayment_thenShouldReturnIamPortResponse() throws IamportResponseException, IOException {
        // Given
        String impUid = "imp12345";
        ReflectionTestUtils.setField(paymentController, "iamportClient", iamportClient);
        IamportResponse<Payment> expectedResponse = new IamportResponse<>();
        given(iamportClient.paymentByImpUid(impUid)).willReturn(expectedResponse);

        // When
        IamportResponse<Payment> result = paymentController.payment(session, impUid);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(iamportClient, times(1)).paymentByImpUid(impUid);
    }

    @Test
    public void givenPaymentService_whenPayment_thenReturnResponseEntity() {
        // Given
        PaymentInfoRequest request = mock(PaymentInfoRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("저장 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = paymentController.payment(request, bindingResult);

        // Then
        verify(applicantApplicationService, times(1)).payment(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenPaymentService_whenGetPaymentInfo_thenReturnResponseEntity() {
        // Given
        Long applicantId =123L;
        Applicant applicant = mock(Applicant.class);
        given(applicantApplicationService.findById(applicantId)).willReturn(applicant);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", applicant.getPaymentInfo());

        // When
        ResponseEntity<?> responseEntity = paymentController.getPaymentInfo(applicantId);

        // Then
        verify(applicantApplicationService, times(1)).findById(applicantId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenPaymentService_whenCancel_thenReturnResponseEntity() throws Exception {
        // Given
        CancelRequest cancelRequest = mock(CancelRequest.class);
        IamPortCancelResponse iamPortCancelResponse = mock(IamPortCancelResponse.class);
        given(iamPortApiService.cancelIamPort(cancelRequest)).willReturn(iamPortCancelResponse);
        given(iamPortCancelResponse.getCode()).willReturn(0);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("결제 취소 성공했습니다.", iamPortCancelResponse.getResponse());

        // When
        ResponseEntity<?> responseEntity = paymentController.cancel(cancelRequest, bindingResult);

        // Then
        verify(iamPortApiService, times(1)).cancelIamPort(cancelRequest);
        verify(applicantApplicationService, times(1)).cancel(cancelRequest.getId(), iamPortCancelResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
}
