package com.example.deukgeun.applicant.mapper;

import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.ApplicantMapper;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ApplicantMapperTest {

    @Autowired
    private ApplicantMapper applicantMapper;

    @Test
    void toApplicantEntityTest() {
        Applicant applicant = new Applicant(
                1L,
                2L,
                3L,
                30000,
                0
        );

        PaymentInfo paymentInfo = PaymentInfo.create(
                "impUid",
                "kakao",
                "phTid",
                "pc",
                10000,
                LocalDateTime.now()
        );

        applicant.setPaymentInfo(paymentInfo);

        applicantMapper.toApplicantEntity(applicant);
    }
}
