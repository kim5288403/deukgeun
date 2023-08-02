package com.example.deukgeun.applicant.infrastructure.persistence.repository;

import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCancelInfoJpaRepository extends JpaRepository<PaymentCancelInfo, Long> {

}
