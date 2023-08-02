package com.example.deukgeun.applicant.domain.model.aggregate;

import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import com.example.deukgeun.job.domain.entity.JobPosting;
import lombok.Getter;

@Getter
public class Applicant {
    private Long id;

    private Long jobPostingId;

    private Long matchInfoId;

    private Long trainerId;

    private Integer supportAmount;

    private Integer isSelected = 0;

    private JobPosting jobPosting;

    private MatchInfo matchInfo;

    public Applicant(
            Long id,
            Long jobPostingId,
            Long matchInfoId,
            Long trainerId,
            Integer supportAmount,
            Integer isSelected,
            JobPosting jobPosting,
            MatchInfo matchInfo
    ) {
        this.id = id;
        this.jobPostingId = jobPostingId;
        this.matchInfoId = matchInfoId;
        this.trainerId = trainerId;
        this.supportAmount = supportAmount;
        this.isSelected = isSelected;
        this.jobPosting = jobPosting;
        this.matchInfo = matchInfo;
    }

    public Applicant(
            Long id,
            Long jobPostingId,
            Long trainerId,
            Integer supportAmount,
            Integer isSelected
    ) {
        this.id = id;
        this.jobPostingId = jobPostingId;
        this.trainerId = trainerId;
        this.supportAmount = supportAmount;
        this.isSelected = isSelected;
    }

    public static Applicant create(
            Long jobPostingId,
            Long trainerId,
            Integer supportAmount,
            Integer isSelected
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new Applicant(id, jobPostingId, trainerId, supportAmount, isSelected);
    }
    public void deleteMatchInfo() {
        this.matchInfo = null;
    }
    public void setMatchInfo(MatchInfo matchInfo) {
        this.matchInfoId = matchInfo.getId();
        this.matchInfo = matchInfo;
    }

    public void updateIsSelect(int isSelected) {
        this.isSelected = isSelected;
    }
}
