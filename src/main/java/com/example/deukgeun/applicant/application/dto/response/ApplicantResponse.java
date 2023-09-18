package com.example.deukgeun.applicant.application.dto.response;

import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.domain.aggregate.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;

@Data
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
    public static class Info {
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
    }
}
