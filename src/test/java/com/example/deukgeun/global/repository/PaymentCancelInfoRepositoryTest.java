package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.PaymentCancelInfo;
import com.example.deukgeun.global.entity.PaymentInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentCancelInfoRepositoryTest {
    @Autowired
    private PaymentCancelInfoRepository paymentCancelInfoRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(paymentCancelInfoRepository);
    }

    @Test
    void givenPaymentCancelInfo_whenSave_thenReturnValid() {
        // Given
        PaymentCancelInfo paymentCancelInfo = PaymentCancelInfo
                .builder()
                .channel("Test")
                .impUid("test")
                .cancel_reason("test")
                .cancel_amount(3000)
                .build();

        // When
        PaymentCancelInfo savePaymentCancelInfo = paymentCancelInfoRepository.save(paymentCancelInfo);

        // Then
        PaymentCancelInfo foundPaymentCancelInfo = paymentCancelInfoRepository.findById(savePaymentCancelInfo.getId()).orElse(null);
        assertNotNull(foundPaymentCancelInfo);
        assertEquals(savePaymentCancelInfo.getImpUid(), foundPaymentCancelInfo.getImpUid());
    }
}
