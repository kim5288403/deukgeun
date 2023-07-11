package com.example.deukgeun.global.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "match_info")
@NoArgsConstructor
public class MatchInfo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_info_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private Long jobPostingId;

    @Column(length = 50, nullable = false)
    private Long applicantId;

    @Column(length = 50, nullable = false)
    private Integer status;

    @OneToOne
    @JoinColumn(name = "applicantId", insertable = false, updatable = false, nullable = false)
    private Applicant applicant;

    @Builder
    public MatchInfo(
            Long id,
            Long jobPostingId,
            Long applicantId,
            Integer status
    ) {
        this.id = id;
        this.applicantId = applicantId;
        this.jobPostingId = jobPostingId;
        this.status = status;
    }
}
