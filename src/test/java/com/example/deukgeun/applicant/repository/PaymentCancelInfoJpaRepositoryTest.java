package com.example.deukgeun.applicant.repository;

import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import com.example.deukgeun.applicant.infrastructure.persistence.repository.PaymentCancelInfoJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentCancelInfoJpaRepositoryTest {
    @Autowired
    private PaymentCancelInfoJpaRepository paymentCancelInfoRepository;

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
