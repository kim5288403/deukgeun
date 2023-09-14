package com.example.deukgeun.applicant.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class PaymentCancelInfo {

    private Long id;

    private String impUid;

    private String channel;

    private String cancelReason;

    private Integer cancelAmount;

    public PaymentCancelInfo(
            Long id,
            String impUid,
            String channel,
            String cancelReason,
            Integer cancelAmount
    ) {
        this.id = id;
        this.impUid = impUid;
        this.channel = channel;
        this.cancelReason = cancelReason;
        this.cancelAmount = cancelAmount;
    }

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
