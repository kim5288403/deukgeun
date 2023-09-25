package com.example.deukgeun.applicant.infrastructure.persistence.mapper;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.dto.response.PaymentResponse;
import com.example.deukgeun.applicant.domain.dto.PaymentCancelInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SavePaymentInfoDTO;
import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.PaymentCancelInfoEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.PaymentInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "iamPortCancelResponse.response.imp_uid", target = "imp_uid")
    @Mapping(source = "iamPortCancelResponse.response.channel", target = "channel")
    @Mapping(source = "iamPortCancelResponse.response.cancel_reason", target = "cancel_reason")
    @Mapping(source = "iamPortCancelResponse.response.cancel_amount", target = "cancel_amount")
    PaymentCancelInfoDTO toPaymentCancelInfoDto(Long applicantId, IamPortCancelResponse iamPortCancelResponse);

    PaymentResponse.Info toPaymentInfoResponse(PaymentInfo paymentInfo);

    @Mapping(source = "paidAt", target = "paidAt")
    SavePaymentInfoDTO toSavePaymentInfoDto(LocalDateTime paidAt, PaymentInfoRequest paymentInfoRequest);

    @Named("toPaymentInfo")
    @Mapping(source = "paymentCancelInfoEntity", target = "paymentCancelInfo", qualifiedByName = "toPaymentCancelInfo", defaultExpression = "java(null)")
    PaymentInfo toPaymentInfo(PaymentInfoEntity paymentInfoEntity);

    @Named("toPaymentInfoEntity")
    @Mapping(source = "paymentCancelInfo", target = "paymentCancelInfoEntity", qualifiedByName = "toPaymentCancelInfoEntity", defaultExpression = "java(null)")
    PaymentInfoEntity toPaymentInfoEntity(PaymentInfo paymentInfo);

    @Named("toPaymentCancelInfo")
    PaymentCancelInfo toPaymentCancelInfo(PaymentCancelInfoEntity paymentCancelInfoEntity);

    @Named("toPaymentCancelInfoEntity")
    PaymentCancelInfoEntity toPaymentCancelInfoEntity(PaymentCancelInfo paymentCancelInfo);
}
