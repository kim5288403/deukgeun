package com.example.deukgeun.applicant.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CancelRequest {
    @NotBlank(message = "결제 아이디는 필수 입력 값입니다.")
    private String impUid;

    @NotNull(message = "결제 금액은 필수 입력 값입니다.")
    private Integer amount;

    @NotBlank(message = "인증 토큰은 필수 입력 값입니다.")
    private String accessToken;
}
