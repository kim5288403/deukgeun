package com.example.deukgeun.trainer.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class License {
    private Long id;

    private String certificateName;

    private String licenseNumber;

    private Long trainerId;

    private LocalDateTime createdDate;

    public static License create(
            String certificateName,
            String licenseNumber,
            Long trainerId
    ) {
      Long id = LongIdGeneratorUtil.gen();
      return new License(id, certificateName, licenseNumber, trainerId, LocalDateTime.now());
    }
}
