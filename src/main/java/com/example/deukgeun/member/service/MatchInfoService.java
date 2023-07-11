package com.example.deukgeun.member.service;

import com.example.deukgeun.global.entity.MatchInfo;
import com.example.deukgeun.member.request.SaveMatchInfoRequest;
import org.springframework.transaction.annotation.Transactional;

public interface MatchInfoService {
    MatchInfo save(SaveMatchInfoRequest saveMatchInfoRequest);
    @Transactional
    void deleteByApplicantId(Long applicantId);

}
