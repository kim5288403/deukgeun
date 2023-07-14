package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    Optional<PaymentInfo> findByApplicantId(Long applicantId);
    Optional<PaymentInfo> findByImpUid(String impUid);
}
