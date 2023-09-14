package com.example.deukgeun.applicant.domain.dto;

import lombok.Data;

@Data
public class SaveMatchInfoDTO {
    private Long applicantId;
    private Long jobId;
    private int status;
}
