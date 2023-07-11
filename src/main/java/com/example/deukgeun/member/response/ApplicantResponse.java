package com.example.deukgeun.member.response;

import com.example.deukgeun.global.entity.Applicant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
