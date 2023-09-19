package com.example.deukgeun.applicant.domain.model.aggregate;

import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Applicant {
    private Long id;

    private Long jobId;

    private Long matchInfoId;

    private Long paymentInfoId;

    private Long trainerId;

    private Integer supportAmount;

    private Integer isSelected;

    private MatchInfo matchInfo;

    private PaymentInfo paymentInfo;

    public static Applicant create(
            Long jobId,
            Long trainerId,
            Integer supportAmount,
            Integer isSelected
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new Applicant(id, jobId, null, null, trainerId, supportAmount, isSelected, null, null);
    }

    public void setMatchInfo(MatchInfo matchInfo) {
        this.matchInfo = matchInfo;
        if (matchInfo == null) {
            this.matchInfoId = null;
        } else {
            this.matchInfoId = matchInfo.getId();
        }
    }
    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
        if (paymentInfo == null) {
            this.paymentInfoId = null;
        } else {
            this.paymentInfoId = paymentInfo.getId();
        }
    }

    public void updateIsSelect(int isSelected) {
        this.isSelected = isSelected;
    }
}
