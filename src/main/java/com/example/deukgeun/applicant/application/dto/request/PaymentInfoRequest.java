package com.example.deukgeun.applicant.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PaymentInfoRequest
{
    @NotNull(message = "지원 내역 아이디는 필수 입력 값입니다.")
    private Long applicantId;

    @NotBlank(message = "결제 아이디는 필수 입력 값입니다.")
    private String impUid;

    @NotBlank(message = "결제 제공자 입력 값입니다.")
    private String pgProvider;

    @NotBlank(message = "거래 번호는 필수 입력 값입니다.")
    private String pgTid;

    @NotBlank(message = "결제 환경은 필수 입력 값입니다.")
    private String channel;

    @NotNull(message = "결제 금액은 필수 입력 값입니다.")
    private Integer amount;

    @NotBlank(message = "결제 일시는 필수 입력 값입니다.")
    private String paidAt;
}
