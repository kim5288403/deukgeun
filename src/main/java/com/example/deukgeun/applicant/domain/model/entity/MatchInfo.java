package com.example.deukgeun.applicant.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class MatchInfo {

    private Long id;

    private Long jobPostingId;

    private Long applicantId;

    private Integer status;

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

    public static MatchInfo create(
            Long jobPostingId,
            Long applicantId,
            Integer status
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new MatchInfo(id, jobPostingId, applicantId, status);
    }
}
