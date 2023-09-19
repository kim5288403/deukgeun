package com.example.deukgeun.applicant.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchInfo {

    private Long id;

    private Integer status;

    public static MatchInfo create(
            Integer status
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new MatchInfo(id, status);
    }
}
