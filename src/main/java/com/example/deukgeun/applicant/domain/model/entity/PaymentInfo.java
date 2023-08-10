package com.example.deukgeun.applicant.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentInfo {
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

    public PaymentInfo (
            Long id,
            String impUid,
            String pgProvider,
            String pgTid,
            String channel,
            Integer amount,
            LocalDateTime paidAt,
            PaymentCancelInfo paymentCancelInfo
            ) {
        this.id = id;
        this.impUid = impUid;
        this.pgProvider = pgProvider;
        this.pgTid = pgTid;
        this.channel = channel;
        this.amount = amount;
        this.paidAt = paidAt;
        this.paymentCancelInfo = paymentCancelInfo;
    }

    public static PaymentInfo create(
            String impUid,
            String pgProvider,
            String pgTid,
            String channel,
            Integer amount,
            LocalDateTime paidAt
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new PaymentInfo(id, impUid, pgProvider, pgTid, channel, amount, paidAt, null);
    }

    public void delete() {
        this.deleteDate = LocalDateTime.now();
    }

    public void setPaymentCancelInfo(PaymentCancelInfo paymentCancelInfo) {
        this.paymentCancelInfo = paymentCancelInfo;
        this.paymentCancelInfoId = paymentCancelInfo.getId();
    }
}