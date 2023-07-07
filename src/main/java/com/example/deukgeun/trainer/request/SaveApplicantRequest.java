package com.example.deukgeun.trainer.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class SaveApplicantRequest {

    @NotNull(message = "공고 아이디는 필수 값입니다.")
    private Long jobPostingId;

    @NotNull(message = "트레이너 아이디는 필수 값입니다.")
    private Long trainerId;

    @NotNull(message = "지원금액은 필수 값입니다.")
    private Integer supportAmount;
}
