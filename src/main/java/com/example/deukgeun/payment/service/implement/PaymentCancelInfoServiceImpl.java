package com.example.deukgeun.payment.service.implement;

import com.example.deukgeun.global.entity.PaymentCancelInfo;
import com.example.deukgeun.global.repository.PaymentCancelInfoRepository;
import com.example.deukgeun.payment.response.IamPortCancelResponse;
import com.example.deukgeun.payment.service.PaymentCancelInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCancelInfoServiceImpl implements PaymentCancelInfoService {
    private final PaymentCancelInfoRepository paymentCancelInfoRepository;

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
