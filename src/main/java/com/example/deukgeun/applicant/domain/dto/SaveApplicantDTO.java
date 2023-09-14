package com.example.deukgeun.applicant.domain.dto;

import lombok.Data;

@Data
public class SaveApplicantDTO {
    private Long jobId;
    private Long trainerId;
    private Integer supportAmount;
}
