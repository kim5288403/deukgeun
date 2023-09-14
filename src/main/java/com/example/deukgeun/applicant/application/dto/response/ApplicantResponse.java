package com.example.deukgeun.applicant.application.dto.response;

import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;

@Data
@AllArgsConstructor
public class ApplicantResponse {
    @Data
    public static class List {
        private Long id;

        private Long trainerId;

        private Long jobId;

        private Integer supportAmount;

        private Integer isSelected;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicantInfo {
        private Long id;

        private Long trainerId;

        private Long jobId;

        private Integer isSelected;

        private String title;

        private String name;

        private String email;

        private String postcode;

        private String roadAddress;

        private Integer amount;

        public ApplicantInfo(Applicant applicant, Member member, Job job) {
            this.id = applicant.getId();
            this.trainerId = applicant.getTrainerId();
            this.jobId = applicant.getJobId();
            this.isSelected = applicant.getIsSelected();
            this.title = job.getTitle();
            this.email = member.getEmail();
            this.name = member.getName();
            this.postcode = job.getAddress().getPostcode();
            this.roadAddress = job.getAddress().getRoadAddress();

            LocalDateTime startDate = job.getStartDate();
            LocalDateTime endDate = job.getEndDate();
            Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
            int supportAmount = applicant.getSupportAmount();
            this.amount = period.getDays() * supportAmount;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentInfoResponse {
        private Long id;

        private String impUid;

        private String pgProvider;

        private String pgTid;

        private String channel;

        private Integer amount;

        private LocalDateTime paidAt;

        private LocalDateTime deleteDate;

        private PaymentCancelInfo paymentCancelInfo;

        private Long paymentCancelInfoId = null;

        public PaymentInfoResponse (PaymentInfo paymentInfo) {
            this.id = paymentInfo.getId();
            this.impUid = paymentInfo.getImpUid();
            this.pgProvider = paymentInfo.getPgProvider();
            this.pgTid = paymentInfo.getPgTid();
            this.channel = paymentInfo.getChannel();
            this.amount = paymentInfo.getAmount();
            this.paidAt = paymentInfo.getPaidAt();
            this.paymentCancelInfo = paymentInfo.getPaymentCancelInfo();
        }
    }


}
