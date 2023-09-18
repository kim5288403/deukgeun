package com.example.deukgeun.applicant.application.dto.response;

import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    @Data
    public static class Info {
        private Long id;
        private String impUid;
        private String pgProvider;
        private String pgTid;
        private String channel;
        private Integer amount;
        private LocalDateTime paidAt;
        private LocalDateTime deleteDate;
        private PaymentCancelInfo paymentCancelInfo;
        private Long paymentCancelInfoId = null;
    }

}
