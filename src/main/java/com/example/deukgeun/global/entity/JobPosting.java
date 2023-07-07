package com.example.deukgeun.global.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "job_posting")
@NoArgsConstructor
public class JobPosting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_posting_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 50)
    private Integer requirementLicense;

    @Column(length = 50)
    private String requirementEtc;

    @Column(length = 50, nullable = false)
    private String postcode;

    @Column(length = 50)
    private String jibunAddress;

    @Column(length = 50)
    private String roadAddress;

    @Column(length = 50)
    private String detailAddress;

    @Column(length = 50)
    private String extraAddress;

    @Column(length = 50)
    private Integer isActive;

    @Column(updatable = false)
    private LocalDateTime startDate;

    @Column(updatable = false)
    private LocalDateTime endDate;

    @Builder
    public JobPosting(
            Long id,
            Long memberId,
            String title,
            Integer requirementLicense,
            String requirementEtc,
            String postcode,
            String jibunAddress,
            String roadAddress,
            String detailAddress,
            String extraAddress,
            Integer isActive,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        this.id = id;
        this.title = title;
        this.memberId = memberId;
        this.requirementLicense = requirementLicense;
        this.requirementEtc = requirementEtc;
        this.postcode = postcode;
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
