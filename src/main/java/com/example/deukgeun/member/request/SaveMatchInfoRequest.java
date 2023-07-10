package com.example.deukgeun.member.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SaveMatchInfoRequest {

    @NotNull(message = "지원 내역 아이디는 필수입니다.")
    private Long applicantId;

    @NotNull(message = "공고 아이디는 필수입니다.")
    private Long jobPostingId;
}
