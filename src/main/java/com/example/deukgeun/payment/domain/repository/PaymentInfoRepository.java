package com.example.deukgeun.payment.domain.repository;

import com.example.deukgeun.payment.domain.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    Optional<PaymentInfo> findByApplicantIdAndDeleteDateIsNull(Long applicantId);
    Optional<PaymentInfo> findByImpUid(String impUid);
}
