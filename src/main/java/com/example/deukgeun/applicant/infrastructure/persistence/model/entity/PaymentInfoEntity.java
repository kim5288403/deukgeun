package com.example.deukgeun.applicant.infrastructure.persistence.model.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_info")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoEntity extends BaseEntity {
    @Id
    @Column(name = "payment_info_id")
    private Long id;

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

    @Column
    private LocalDateTime deleteDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paymentCancelInfoId", insertable = false, updatable = false)
    private PaymentCancelInfoEntity paymentCancelInfoEntity;

    private Long paymentCancelInfoId;
}
