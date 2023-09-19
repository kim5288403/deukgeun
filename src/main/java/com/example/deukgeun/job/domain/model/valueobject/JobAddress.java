package com.example.deukgeun.job.domain.model.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobAddress {
    private String postcode;
    private String jibunAddress;
    private String roadAddress;
    private String detailAddress;
    private String extraAddress;
}
