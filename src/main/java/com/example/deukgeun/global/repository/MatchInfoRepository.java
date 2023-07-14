package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.MatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {

    boolean existsByJobPostingIdAndDeleteDateIsNull(Long jobPostingId);

    void deleteByApplicantId(Long applicantId);

    MatchInfo findByJobPostingId(Long jobPostingId);

    Optional<MatchInfo> findByApplicantId(Long applicant);

}
