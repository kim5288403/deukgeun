package com.example.deukgeun.job.domain.dto;

import lombok.Data;

@Data
public class SaveJobDTO {
    private Long memberId;
    private String title;
    private Integer requirementLicense;
    private String requirementEtc;
    private String postcode;
    private String jibunAddress;
    private String roadAddress;
    private String detailAddress;
    private String extraAddress;
    private String startDate;
    private String endDate;
}
