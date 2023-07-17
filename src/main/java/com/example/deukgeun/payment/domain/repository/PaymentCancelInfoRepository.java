package com.example.deukgeun.payment.domain.repository;

import com.example.deukgeun.payment.domain.entity.PaymentCancelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCancelInfoRepository extends JpaRepository<PaymentCancelInfo, Long> {

}
