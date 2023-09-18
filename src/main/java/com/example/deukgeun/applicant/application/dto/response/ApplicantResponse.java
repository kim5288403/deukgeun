package com.example.deukgeun.applicant.application.dto.response;

import lombok.Data;

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
