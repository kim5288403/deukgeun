package com.example.deukgeun.applicant.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class MatchInfo {

    private Long id;

    private Long jobPostingId;

    private Integer status;

    public MatchInfo(
            Long id,
            Long jobPostingId,
            Integer status
    ) {
        this.id = id;
        this.jobPostingId = jobPostingId;
        this.status = status;
    }

    public static MatchInfo create(
            Long jobPostingId,
            Integer status
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new MatchInfo(id, jobPostingId, status);
    }
}
