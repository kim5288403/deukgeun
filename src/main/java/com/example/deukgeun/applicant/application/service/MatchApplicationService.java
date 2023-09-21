package com.example.deukgeun.applicant.application.service;

import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;

public interface MatchApplicationService {
    void deleteMatchInfoById(Long id);
    void isAnnouncementMatchedByJobId(Long jobId);
    Applicant saveMatchInfo(SaveMatchInfoRequest saveMatchInfoRequest, int status);
}
