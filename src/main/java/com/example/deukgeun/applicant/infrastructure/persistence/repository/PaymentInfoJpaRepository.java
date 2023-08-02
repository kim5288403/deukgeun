package com.example.deukgeun.applicant.infrastructure.persistence.repository;

import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentInfoJpaRepository extends JpaRepository<PaymentInfo, Long> {
    Optional<PaymentInfo> findByApplicantIdAndDeleteDateIsNull(Long applicantId);
    Optional<PaymentInfo> findByImpUid(String impUid);
}
