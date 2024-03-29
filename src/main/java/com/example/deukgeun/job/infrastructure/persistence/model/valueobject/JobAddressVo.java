package com.example.deukgeun.job.infrastructure.persistence.model.valueobject;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobAddressVo {
    @Column(length = 50, nullable = false)
    private String postcode;

    @Column(length = 50)
    private String jibunAddress;

    @Column(length = 50)
    private String roadAddress;

    @Column(length = 50)
    private String detailAddress;

    @Column(length = 50)
    private String extraAddress;
}
