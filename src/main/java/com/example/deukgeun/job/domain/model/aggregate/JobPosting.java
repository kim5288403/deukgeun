package com.example.deukgeun.job.domain.model.aggregate;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class JobPosting {
    private Long id;

    private Long memberId;

    private String title;

    private Integer requirementLicense;

    private String requirementEtc;

    private Integer isActive;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Address address;

    public JobPosting(
            Long id,
            Long memberId,
            String title,
            Integer requirementLicense,
            String requirementEtc,
            Address address,
            Integer isActive,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.requirementLicense = requirementLicense;
        this.requirementEtc = requirementEtc;
        this.address = address;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static JobPosting create(
            Long memberId,
            String title,
            Integer requirementLicense,
            String requirementEtc,
            Address address,
            Integer isActive,
            String startDate,
            String endDate
    ) {
        Long id = LongIdGeneratorUtil.gen();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime convertStartDate = LocalDateTime.parse(startDate, formatter);
        LocalDateTime convertEndDate = LocalDateTime.parse(endDate, formatter);
        return new JobPosting(
                id,
                memberId,
                title,
                requirementLicense,
                requirementEtc,
                address,
                isActive,
                convertStartDate,
                convertEndDate
        );
    }

    public void updateIsActive(int isActive) {
        this.isActive = isActive;
    }

}
