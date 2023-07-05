package com.example.deukgeun.commom.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "matching_information")
public class MatchingInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_information_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private Long jobPostingId;

    @Column(length = 50, nullable = false)
    private Long applicantId;

    @Column(length = 50, nullable = false)
    private Integer status;

    @Column(updatable = false)
    private LocalDateTime matchData;

    @Builder
    public MatchingInformation(
            Long id,
            Long jobPostingId,
            Long applicantId,
            Integer status,
            LocalDateTime match_date
    ) {
        this.id = id;
        this.applicantId = applicantId;
        this.jobPostingId = jobPostingId;
        this.status = status;
        this.matchData = match_date;
    }
}
