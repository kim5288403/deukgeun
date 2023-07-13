package com.example.deukgeun.payment.controller;

import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.main.response.RestResponse;
import com.example.deukgeun.payment.request.PaymentInfoRequest;
import com.example.deukgeun.payment.service.PaymentService;
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
    private PaymentService paymentService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private IamportClient iamportClient;
    @Mock
    private HttpSession session;

    @Test
    public void givenImpUid_whenPayment_thenShouldReturnIamportResponse() throws IamportResponseException, IOException {
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
    public void givenPaymentService_whenSave_thenReturnResponseEntity() {
        // Given
        PaymentInfoRequest request = mock(PaymentInfoRequest.class);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("저장 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = paymentController.save(request, bindingResult);

        // Then
        verify(paymentService, times(1)).save(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
}
