package com.example.deukgeun.applicant.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SaveMatchInfoRequest {

    @NotNull(message = "지원 내역 아이디는 필수입니다.")
    private Long applicantId;

    @NotNull(message = "공고 아이디는 필수입니다.")
    private Long jobId;
}
