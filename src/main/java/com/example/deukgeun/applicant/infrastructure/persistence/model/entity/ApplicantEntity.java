package com.example.deukgeun.applicant.infrastructure.persistence.model.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobPostingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "applicant")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private Long trainerId;

    @Column(length = 50, nullable = false)
    private Integer supportAmount;

    @Column(length = 50)
    private Integer isSelected;

    @ManyToOne
    @JoinColumn(name = "jobPostingId", insertable = false, updatable = false)
    private JobPostingEntity jobPostingEntity;

    private Long jobPostingId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "matchInfoId", insertable = false, updatable = false)
    private MatchInfoEntity matchInfoEntity;

    private Long matchInfoId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paymentInfoId", insertable = false, updatable = false)
    private PaymentInfoEntity paymentInfoEntity;

    private Long paymentInfoId;

}
