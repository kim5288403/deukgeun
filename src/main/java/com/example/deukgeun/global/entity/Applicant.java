package com.example.deukgeun.global.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "applicant")
public class Applicant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private Long jobPostingId;

    @Column(length = 50, nullable = false)
    private Long trainerId;

    @Column(length = 50, nullable = false)
    private Integer supportAmount;

    @Builder
    public Applicant(
            Long id,
            Long jobPostingId,
            Long trainerId,
            Integer supportAmount
    ) {
        this.id = id;
        this.jobPostingId = jobPostingId;
        this.trainerId = trainerId;
        this.supportAmount = supportAmount;
    }
}
