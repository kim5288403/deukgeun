package com.example.deukgeun.applicant.domain.model.aggregate;

import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import lombok.Getter;

@Getter
public class Applicant {
    private Long id;

    private Long jobId;

    private Long matchInfoId = null;

    private Long paymentInfoId = null;

    private Long trainerId;

    private Integer supportAmount;

    private Integer isSelected = 0;

    private Job job;

    private MatchInfo matchInfo = null;

    private PaymentInfo paymentInfo = null;

    public Applicant(
            Long id,
            Long jobId,
            Long matchInfoId,
            Long paymentInfoId,
            Long trainerId,
            Integer supportAmount,
            Integer isSelected,
            Job job,
            MatchInfo matchInfo,
            PaymentInfo paymentInfo
    ) {
        this.id = id;
        this.jobId = jobId;
        this.matchInfoId = matchInfoId;
        this.paymentInfoId = paymentInfoId;
        this.trainerId = trainerId;
        this.supportAmount = supportAmount;
        this.isSelected = isSelected;
        this.job = job;
        this.matchInfo = matchInfo;
        this.paymentInfo = paymentInfo;
    }

    public Applicant(
            Long id,
            Long jobId,
            Long trainerId,
            Integer supportAmount,
            Integer isSelected
    ) {
        this.id = id;
        this.jobId = jobId;
        this.trainerId = trainerId;
        this.supportAmount = supportAmount;
        this.isSelected = isSelected;
    }

    public static Applicant create(
            Long jobId,
            Long trainerId,
            Integer supportAmount,
            Integer isSelected
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new Applicant(id, jobId, trainerId, supportAmount, isSelected);
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
