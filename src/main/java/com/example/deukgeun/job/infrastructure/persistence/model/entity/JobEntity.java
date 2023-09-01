package com.example.deukgeun.job.infrastructure.persistence.model.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import com.example.deukgeun.job.infrastructure.persistence.model.valueobject.JobAddressVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "job")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobEntity extends BaseEntity {

    @Id
    @Column(name = "job_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 50)
    private Integer requirementLicense;

    @Column(length = 50)
    private String requirementEtc;

    @Column(length = 50)
    private Integer isActive;

    @Column(updatable = false)
    private LocalDateTime startDate;

    @Column(updatable = false)
    private LocalDateTime endDate;

    @Embedded
    private JobAddressVo jobAddressVo;
}
