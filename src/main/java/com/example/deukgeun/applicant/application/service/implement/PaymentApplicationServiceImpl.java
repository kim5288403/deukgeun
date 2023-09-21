package com.example.deukgeun.applicant.application.service.implement;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.dto.response.PaymentResponse;
import com.example.deukgeun.applicant.application.service.PaymentApplicationService;
import com.example.deukgeun.applicant.domain.dto.PaymentCancelInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SavePaymentInfoDTO;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaymentApplicationServiceImpl implements PaymentApplicationService {
    private final ApplicantDomainService applicantDomainService;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponse.Info getPaymentInfo(Long id) {
        return paymentMapper.toPaymentInfoResponse(applicantDomainService.findById(id).getPaymentInfo());
    }

    /**
     * PaymentInfoRequest 사용하여 결제 정보를 처리하고 지원자 정보를 반환하는 메서드입니다.
     *
     * @param request 결제 정보를 나타내는 PaymentInfoRequest 객체
     */
    @Override
    public void savePaymentInfo(PaymentInfoRequest request) {
        // 날짜 형식 지정을 위한 DateTimeFormatter 객체 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        // 요청에서 'paidAt' 값을 파싱하여 LocalDateTime 객체로 변환
        LocalDateTime paidAt = LocalDateTime.parse(request.getPaidAt(), formatter);

        SavePaymentInfoDTO savePaymentInfoDTO = paymentMapper.toSavePaymentInfoDto(paidAt, request);

        applicantDomainService.savePaymentInfo(savePaymentInfoDTO);
    }

    /**
     * 지정된 ID를 사용하여 결제를 취소하는 메서드입니다.
     *
     * @param id 결제를 취소할 지원자(ID)의 고유 ID
     * @param iamPortCancelResponse IamPort 결제 취소 응답 객체
     */
    @Override
    public void updatePaymentCancelInfoById(Long id, IamPortCancelResponse iamPortCancelResponse) {
        PaymentCancelInfoDTO paymentCancelInfoDTO = paymentMapper.toPaymentCancelInfoDto(id, iamPortCancelResponse);

        applicantDomainService.updatePaymentCancelInfoById(paymentCancelInfoDTO);
    }
}
