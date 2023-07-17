package com.example.deukgeun.payment.domain.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PaymentInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_info_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private Long applicantId;

    @Column(length = 100, nullable = false)
    private String impUid;

    @Column(length = 100, nullable = false)
    private String pgProvider;

    @Column(length = 100, nullable = false)
    private String pgTid;

    @Column(length = 100, nullable = false)
    private String channel;

    @Column(length = 50, nullable = false)
    private Integer amount;

    @Column(updatable = false)
    private LocalDateTime paidAt;

    @Builder
    public PaymentInfo(Long id,
                       Long applicantId,
                       String impUid,
                       String pgProvider,
                       String pgTid,
                       String channel,
                       Integer amount,
                       LocalDateTime paidAt) {
        this.id = id;
        this.applicantId = applicantId;
        this.impUid = impUid;
        this.pgProvider = pgProvider;
        this.pgTid = pgTid;
        this.channel = channel;
        this.amount = amount;
        this.paidAt = paidAt;
    }


}
