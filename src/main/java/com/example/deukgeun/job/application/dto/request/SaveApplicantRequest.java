package com.example.deukgeun.job.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SaveApplicantRequest {

    @NotNull(message = "공고 아이디는 필수 값입니다.")
    private Long jobPostingId;

    @NotNull(message = "지원금액은 필수 값입니다.")
    private Integer supportAmount;
}