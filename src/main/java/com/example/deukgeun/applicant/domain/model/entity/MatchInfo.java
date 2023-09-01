package com.example.deukgeun.applicant.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class MatchInfo {

    private Long id;

    private Long jobId;

    private Integer status;

    public MatchInfo(
            Long id,
            Long jobId,
            Integer status
    ) {
        this.id = id;
        this.jobId = jobId;
        this.status = status;
    }

    public static MatchInfo create(
            Long jobId,
            Integer status
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new MatchInfo(id, jobId, status);
    }
}
