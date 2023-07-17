package com.example.deukgeun.job.domain.service;

import com.example.deukgeun.job.domain.entity.MatchInfo;
import com.example.deukgeun.job.application.dto.request.SaveMatchInfoRequest;
import org.springframework.transaction.annotation.Transactional;

public interface MatchService {
    MatchInfo save(SaveMatchInfoRequest saveMatchInfoRequest);
    @Transactional
    void deleteByApplicantId(Long applicantId);

    void isAnnouncementMatchedByJobPostingId(Long jobPostingId);

}
