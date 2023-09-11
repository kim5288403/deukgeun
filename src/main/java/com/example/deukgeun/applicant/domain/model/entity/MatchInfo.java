package com.example.deukgeun.applicant.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class MatchInfo {

    private Long id;

    private Integer status;

    public MatchInfo(
            Long id,
            Integer status
    ) {
        this.id = id;
        this.status = status;
    }

    public static MatchInfo create(
            Integer status
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new MatchInfo(id, status);
    }
}
