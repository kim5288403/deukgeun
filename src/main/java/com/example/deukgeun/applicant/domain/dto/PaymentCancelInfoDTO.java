package com.example.deukgeun.applicant.domain.dto;

import lombok.Data;

@Data
public class PaymentCancelInfoDTO {
    private Long ApplicantId;
    private Integer code;
    private String message;
    private String imp_uid;
    private String channel;
    private String cancel_reason;
    private Integer cancel_amount;
}
