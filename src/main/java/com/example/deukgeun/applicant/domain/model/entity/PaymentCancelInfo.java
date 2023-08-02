package com.example.deukgeun.applicant.domain.model.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor
public class PaymentCancelInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_cancel_info_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String impUid;

    @Column(length = 100, nullable = false)
    private String channel;

    @Column(length = 100, nullable = false)
    private String cancel_reason;

    @Column(length = 100, nullable = false)
    private Integer cancel_amount;

    @Builder
    public PaymentCancelInfo(Long id,
                       String impUid,
                       String channel,
                       String cancel_reason,
                       Integer cancel_amount) {
        this.id = id;
        this.impUid = impUid;
        this.cancel_reason = cancel_reason;
        this.channel = channel;
        this.cancel_amount = cancel_amount;
    }
}
