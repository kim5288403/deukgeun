package com.example.deukgeun.trainer.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class License {
    private Long id;

    private String certificateName;

    private String licenseNumber;

    private Long trainerId;

    private LocalDateTime createdDate;

    public License(
            Long id,
            String certificateName,
            String licenseNumber,
            Long trainerId
    ) {
        this.id = id;
        this.certificateName = certificateName;
        this.licenseNumber = licenseNumber;
        this.trainerId = trainerId;
    }

    public License(
            Long id,
            String certificateName,
            String licenseNumber,
            Long trainerId,
            LocalDateTime createdDate
    ) {
        this.id = id;
        this.certificateName = certificateName;
        this.licenseNumber = licenseNumber;
        this.trainerId = trainerId;
        this.createdDate = createdDate;
    }
    public static License create(
            String certificateName,
            String licenseNumber,
            Long trainerId
    ) {
      Long id = LongIdGeneratorUtil.gen();
      return new License(id, certificateName, licenseNumber, trainerId);
    }
}
