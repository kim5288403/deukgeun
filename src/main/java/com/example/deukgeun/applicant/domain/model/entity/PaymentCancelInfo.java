package com.example.deukgeun.applicant.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCancelInfo {

    private Long id;

    private String impUid;

    private String channel;

    private String cancelReason;

    private Integer cancelAmount;

    public static PaymentCancelInfo create(
            String impUid,
            String channel,
            String cancel_reason,
            Integer cancelAmount
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new PaymentCancelInfo(id, impUid, channel, cancel_reason, cancelAmount);
    }
}
