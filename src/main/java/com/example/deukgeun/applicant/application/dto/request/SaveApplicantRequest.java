package com.example.deukgeun.applicant.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SaveApplicantRequest {

    @NotNull(message = "공고 아이디는 필수 값입니다.")
    private Long jobPostingId;

    @NotNull(message = "지원금액은 필수 값입니다.")
    private Integer supportAmount;
}
