package com.example.deukgeun.applicant.application.dto.response;

import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListResponse {
        private Long id;

        private Long trainerId;

        private Long jobPostingId;

        private Integer supportAmount;

        private Integer isSelected;

        public ListResponse(Applicant applicant) {
            this.id = applicant.getId();
            this.trainerId = applicant.getTrainerId();
            this.jobPostingId = applicant.getJobPostingId();
            this.supportAmount = applicant.getSupportAmount();
            this.isSelected = applicant.getIsSelected();
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicantInfo {
        private Long id;

        private Long trainerId;

        private Long jobPostingId;

        private Integer isSelected;

        private String title;

        private String name;

        private String email;

        private String postcode;

        private String roadAddress;

        private Integer amount;

        public ApplicantInfo(Applicant applicant, Member member) {
            this.id = applicant.getId();
            this.trainerId = applicant.getTrainerId();
            this.jobPostingId = applicant.getJobPostingId();
            this.isSelected = applicant.getIsSelected();
            this.title = applicant.getJobPosting().getTitle();
            this.email = member.getEmail();
            this.name = member.getName();
            this.postcode = applicant.getJobPosting().getAddress().getPostcode();
            this.roadAddress = applicant.getJobPosting().getAddress().getRoadAddress();

            LocalDateTime startDate = applicant.getJobPosting().getStartDate();
            LocalDateTime endDate = applicant.getJobPosting().getEndDate();
            Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
            int supportAmount = applicant.getSupportAmount();
            this.amount = period.getDays() * supportAmount;
        }
    }
}
