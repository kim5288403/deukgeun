package com.example.deukgeun.global.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "applicant")
@NoArgsConstructor
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

    @Column(length = 50)
    private Integer isSelected = 0;

    @Builder
    public Applicant(
            Long id,
            Long jobPostingId,
            Long trainerId,
            Integer supportAmount,
            Integer isSelected
    ) {
        this.id = id;
        this.jobPostingId = jobPostingId;
        this.trainerId = trainerId;
        this.supportAmount = supportAmount;
        this.isSelected = isSelected;
    }

    public void updateIsSelect(int isSelected) {
        this.isSelected = isSelected;
    }

}
