package com.example.deukgeun.applicant.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SavePaymentInfoDTO {
    private Long applicantId;
    private String impUid;
    private String pgProvider;
    private String pgTid;
    private String channel;
    private Integer amount;
    private LocalDateTime paidAt;
}
