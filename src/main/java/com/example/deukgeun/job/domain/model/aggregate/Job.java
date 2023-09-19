package com.example.deukgeun.job.domain.model.aggregate;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import com.example.deukgeun.job.domain.model.valueobject.JobAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class Job {
    private Long id;

    private Long memberId;

    private String title;

    private Integer requirementLicense;

    private String requirementEtc;

    private Integer isActive;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private JobAddress jobAddress;

    public static Job create(
            Long memberId,
            String title,
            Integer requirementLicense,
            String requirementEtc,
            JobAddress jobAddress,
            Integer isActive,
            String startDate,
            String endDate
    ) {
        Long id = LongIdGeneratorUtil.gen();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime convertStartDate = LocalDateTime.parse(startDate, formatter);
        LocalDateTime convertEndDate = LocalDateTime.parse(endDate, formatter);
        return new Job(
                id,
                memberId,
                title,
                requirementLicense,
                requirementEtc,
                isActive,
                convertStartDate,
                convertEndDate,
                jobAddress
                );
    }

    public void updateIsActive(int isActive) {
        this.isActive = isActive;
    }

}
