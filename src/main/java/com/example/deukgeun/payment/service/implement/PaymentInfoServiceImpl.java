package com.example.deukgeun.payment.service.implement;

import com.example.deukgeun.global.entity.PaymentInfo;
import com.example.deukgeun.global.repository.PaymentInfoRepository;
import com.example.deukgeun.payment.request.PaymentInfoRequest;
import com.example.deukgeun.payment.service.PaymentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaymentInfoServiceImpl implements PaymentInfoService {

    private final PaymentInfoRepository paymentInfoRepository;

    @Override
    public PaymentInfo save(PaymentInfoRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

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

    @Override
    public PaymentInfo getPaymentInfoByApplicantId(Long applicantId) {
        return paymentInfoRepository.findByApplicantId(applicantId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지않는 결제 정보입니다."));
    }

    @Override
    public void deleteByImpUid(String impUid) {
        PaymentInfo paymentInfo = paymentInfoRepository.findByImpUid(impUid).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 결제 정보 입니다.")
        );

        paymentInfo.delete();
        paymentInfoRepository.save(paymentInfo);
    }
}
