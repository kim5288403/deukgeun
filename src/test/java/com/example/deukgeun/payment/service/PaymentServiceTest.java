package com.example.deukgeun.payment.service;

import com.example.deukgeun.global.entity.PaymentInfo;
import com.example.deukgeun.global.repository.PaymentInfoRepository;
import com.example.deukgeun.payment.request.PaymentInfoRequest;
import com.example.deukgeun.payment.service.implement.PaymentServiceImpl;
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
public class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;
    @Mock
    private PaymentInfoRepository paymentInfoRepository;

    @Test
    public void givenPaymentInfoRequest_whenSave_thenShouldReturnSavedPaymentInfo() {
        // Given
        PaymentInfoRequest request = new PaymentInfoRequest();
        request.setApplicantId(12345L);
        request.setImpUid("imp12345");
        request.setPgProvider("pg-provider");
        request.setPgTid("pg-tid");
        request.setChannel("channel");
        request.setAmount(1000);
        request.setPaidAt("2023-07-14T10:30:00.000+00:00");

        PaymentInfo expectedPaymentInfo = new PaymentInfo();
        given(paymentInfoRepository.save(any(PaymentInfo.class))).willReturn(expectedPaymentInfo);

        // When
        PaymentInfo result = paymentService.save(request);

        // Then
        assertNotNull(result);
        assertEquals(expectedPaymentInfo, result);
        verify(paymentInfoRepository, times(1)).save(any(PaymentInfo.class));
    }

}
