package com.example.deukgeun.job.application.dto.response;

import com.example.deukgeun.job.domain.model.valueobject.JobAddress;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobResponse {
    @Data
    public static class List {
        private Long id;
        private Long memberId;
        private String title;
        private String startDate;
        private String endDate;
        private String address;
    }

    @Data
    public static class Detail {
        private Long id;
        private Long memberId;
        private String title;
        private Integer requirementLicense;
        private String requirementEtc;
        private Integer isActive;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private JobAddress jobAddress;
    }
}
