package com.example.deukgeun.payment.service.implement;

import com.example.deukgeun.global.entity.PaymentInfo;
import com.example.deukgeun.global.repository.PaymentInfoRepository;
import com.example.deukgeun.payment.request.PaymentInfoRequest;
import com.example.deukgeun.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentInfoRepository paymentInfoRepository;
    @Override
    public PaymentInfo save(PaymentInfoRequest request) {
        System.out.println(request.getPaidAt());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PaymentInfo paymentInfo = PaymentInfo
                .builder()
                .applicantId(request.getApplicantId())
                .impUid(request.getImpUid())
                .pgProvider(request.getPgProvider())
                .pgTid(request.getPgTid())
                .channel(request.getChannel())
                .amount(request.getAmount())
                .paidAt(LocalDateTime.parse(request.getPaidAt(), formatter))
                .build();

        return paymentInfoRepository.save(paymentInfo);
    }
}
