package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.MatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {

    boolean existsByJobPostingId(Long jobPostingId);

    void deleteByApplicantId(Long applicantId);

    MatchInfo findByJobPostingId(Long jobPostingId);
}
