package com.example.deukgeun.job.domain.dto;

import com.example.deukgeun.job.domain.model.valueobject.JobAddress;
import lombok.Data;

@Data
public class SaveJobDTO {
    private Long memberId;
    private String title;
    private Integer requirementLicense;
    private String requirementEtc;
    private String postcode;
    private JobAddress jobAddress;
    private String startDate;
    private String endDate;
}
