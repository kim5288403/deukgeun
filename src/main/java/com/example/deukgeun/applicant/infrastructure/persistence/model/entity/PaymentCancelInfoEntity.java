package com.example.deukgeun.applicant.infrastructure.persistence.model.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@Table(name = "payment_cancel_info")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelInfoEntity extends BaseEntity {
    @Id
    @Column(name = "payment_cancel_info_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String impUid;

    @Column(length = 100, nullable = false)
    private String channel;

    @Column(length = 100, nullable = false)
    private String cancelReason;

    @Column(length = 100, nullable = false)
    private Integer cancelAmount;
}
