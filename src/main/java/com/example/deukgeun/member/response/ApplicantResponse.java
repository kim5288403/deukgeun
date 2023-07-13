package com.example.deukgeun.member.response;

import com.example.deukgeun.global.entity.Applicant;
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

        public ApplicantInfo(Applicant applicant) {
            this.id = applicant.getId();
            this.trainerId = applicant.getTrainerId();
            this.jobPostingId = applicant.getJobPostingId();
            this.isSelected = applicant.getIsSelected();
            this.title = applicant.getJobPosting().getTitle();
            this.email = applicant.getJobPosting().getMember().getEmail();
            this.postcode = applicant.getJobPosting().getPostcode();
            this.roadAddress = applicant.getJobPosting().getRoadAddress();

            LocalDateTime startDate = applicant.getJobPosting().getStartDate();
            LocalDateTime endDate = applicant.getJobPosting().getEndDate();
            Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
            int supportAmount = applicant.getSupportAmount();
            this.amount = period.getDays() * supportAmount;
        }
    }
}
