package com.example.deukgeun.global.repository;

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
public class PaymentRepositoryTest {

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(paymentInfoRepository);
    }
    @Test
    void givenPaymentInfo_whenSave_thenReturnValid() {
        // Given
        PaymentInfo paymentInfo = PaymentInfo
                .builder()
                .applicantId(123L)
                .pgTid("test")
                .paidAt(LocalDateTime.now())
                .amount(3000)
                .channel("Test")
                .pgProvider("test")
                .impUid("test")
                .build();

        // When
        PaymentInfo savePaymentInfo = paymentInfoRepository.save(paymentInfo);

        // Then
        PaymentInfo foundPaymentInfo = paymentInfoRepository.findById(savePaymentInfo.getId()).orElse(null);
        assertNotNull(foundPaymentInfo);
        assertEquals(savePaymentInfo.getImpUid(), foundPaymentInfo.getImpUid());
    }

}
