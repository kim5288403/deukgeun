package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.PaymentInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void givenExistingImpUid_whenFindByImpUid_ReturnValid() {
        // Given
        String impUid = "sample_imp_uid";
        PaymentInfo paymentInfo = PaymentInfo
                .builder()
                .applicantId(123L)
                .pgTid("test")
                .paidAt(LocalDateTime.now())
                .amount(3000)
                .channel("Test")
                .pgProvider("test")
                .impUid(impUid)
                .build();
        paymentInfoRepository.save(paymentInfo);

        // When
        PaymentInfo result = paymentInfoRepository.findByImpUid(impUid).orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(result.getImpUid(), impUid);
    }

    @Test
    public void givenNonExistingImpUid_whenFindByImpUid_ReturnValid() {
        // Given
        String impUid = "non_existing_imp_uid";

        // When
        PaymentInfo optionalPaymentInfo = paymentInfoRepository.findByImpUid(impUid).orElse(null);

        // Then
        assertNull(optionalPaymentInfo);
    }

    @Test
    void givenExistingApplicantId_whenFindByApplicantId_ReturnValid() {
        // Given
        Long applicantId = 123L;
        PaymentInfo paymentInfo = PaymentInfo
                .builder()
                .applicantId(applicantId)
                .pgTid("test")
                .paidAt(LocalDateTime.now())
                .amount(3000)
                .channel("Test")
                .pgProvider("test")
                .impUid("sample_imp_uid")
                .build();
        paymentInfoRepository.save(paymentInfo);

        // When
        PaymentInfo result = paymentInfoRepository.findByApplicantId(applicantId).orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(result.getApplicantId(), applicantId);
    }

    @Test
    public void givenNonExistingApplicantId_whenFindByApplicantId_ReturnValid() {
        // Given
        Long applicantId = 123L;

        // When
        PaymentInfo optionalPaymentInfo = paymentInfoRepository.findByApplicantId(applicantId).orElse(null);

        // Then
        assertNull(optionalPaymentInfo);
    }

}
