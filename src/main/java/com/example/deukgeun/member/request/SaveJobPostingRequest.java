package com.example.deukgeun.member.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveJobPostingRequest {

    @NotBlank(message = "이름 필수 입력 값입니다.")
    private String title;

    private Integer requirementLicense;

    private String requirementEtc;

    @NotBlank(message = "우편번호는 필수 입력 값입니다.")
    private String postcode;

    private String jibunAddress;

    private String roadAddress;

    private String detailAddress;

    private String extraAddress;

    @NotBlank(message = "시작일은 필수 값입니다.")
    private String startDate;

    @NotBlank(message = "종료일은 필수 값입니다.")
    private String endDate;
}
