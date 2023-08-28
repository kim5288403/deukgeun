package com.example.deukgeun.applicant.domain.model.aggregate;

import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import com.example.deukgeun.jobPosting.domain.model.aggregate.JobPosting;
import lombok.Getter;

@Getter
public class Applicant {
    private Long id;

    private Long jobPostingId;

    private Long matchInfoId = null;

    private Long paymentInfoId = null;

    private Long trainerId;

    private Integer supportAmount;

    private Integer isSelected = 0;

    private JobPosting jobPosting;

    private MatchInfo matchInfo = null;

    private PaymentInfo paymentInfo = null;

    public Applicant(
            Long id,
            Long jobPostingId,
            Long matchInfoId,
            Long paymentInfoId,
            Long trainerId,
            Integer supportAmount,
            Integer isSelected,
            JobPosting jobPosting,
            MatchInfo matchInfo,
            PaymentInfo paymentInfo
    ) {
        this.id = id;
        this.jobPostingId = jobPostingId;
        this.matchInfoId = matchInfoId;
        this.paymentInfoId = paymentInfoId;
        this.trainerId = trainerId;
        this.supportAmount = supportAmount;
        this.isSelected = isSelected;
        this.jobPosting = jobPosting;
        this.matchInfo = matchInfo;
        this.paymentInfo = paymentInfo;
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
        this.matchInfoId = null;
    }

    public void deletePaymentInfo() {
        this.paymentInfo.delete();
        this.paymentInfoId = null;
    }

    public void setMatchInfo(MatchInfo matchInfo) {
        this.matchInfo = matchInfo;
        this.matchInfoId = matchInfo.getId();
    }
    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
        this.paymentInfoId = paymentInfo.getId();
    }

    public void updateIsSelect(int isSelected) {
        this.isSelected = isSelected;
    }
}
