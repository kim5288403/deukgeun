package com.example.deukgeun.payment.service;

import com.example.deukgeun.payment.domain.entity.PaymentCancelInfo;
import com.example.deukgeun.payment.domain.repository.PaymentCancelInfoRepository;
import com.example.deukgeun.payment.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.payment.infrastructure.persistence.PaymentCancelInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class PaymentCancelInfoServiceTest {

    @InjectMocks
    private PaymentCancelInfoServiceImpl paymentCancelInfoService;
    @Mock
    private PaymentCancelInfoRepository paymentCancelInfoRepository;

    @Test
    public void givenIamPortCancelResponse_whenSave_thenShouldReturnSavedPaymentCancelInfo() {
        // Given
        IamPortCancelResponse response = new IamPortCancelResponse();
        response.setResponse(new IamPortCancelResponse.Response());
        response.getResponse().setImp_uid("imp12345");
        response.getResponse().setChannel("channel");
        response.getResponse().setCancel_amount(1000);
        response.getResponse().setCancel_reason("test");

        PaymentCancelInfo expectedPaymentCancelInfo = new PaymentCancelInfo();
        given(paymentCancelInfoRepository.save(any(PaymentCancelInfo.class))).willReturn(expectedPaymentCancelInfo);

        // When
        PaymentCancelInfo result = paymentCancelInfoService.save(response);

        // Then
        assertNotNull(result);
        assertEquals(expectedPaymentCancelInfo, result);
        verify(paymentCancelInfoRepository, times(1)).save(any(PaymentCancelInfo.class));
    }

}
