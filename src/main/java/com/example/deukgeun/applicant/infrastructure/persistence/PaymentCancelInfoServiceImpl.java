package com.example.deukgeun.applicant.infrastructure.persistence;

import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import com.example.deukgeun.applicant.infrastructure.persistence.repository.PaymentCancelInfoJpaRepository;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.domain.service.PaymentCancelInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCancelInfoServiceImpl implements PaymentCancelInfoService {
    private final PaymentCancelInfoJpaRepository paymentCancelInfoRepository;

    @Override
    public PaymentCancelInfo save(IamPortCancelResponse iamPortCancelResponse) {
        PaymentCancelInfo paymentCancelInfo = PaymentCancelInfo
                .builder()
                .cancel_amount(iamPortCancelResponse.getResponse().getCancel_amount())
                .cancel_reason(iamPortCancelResponse.getResponse().getCancel_reason())
                .impUid(iamPortCancelResponse.getResponse().getImp_uid())
                .channel(iamPortCancelResponse.getResponse().getChannel())
                .build();

        return paymentCancelInfoRepository.save(paymentCancelInfo);
    }
}
