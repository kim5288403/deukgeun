package com.example.deukgeun.job.domain.model.valueobject;

import lombok.Data;

@Data
public class JobAddress {
    private String postcode;
    private String jibunAddress;
    private String roadAddress;
    private String detailAddress;
    private String extraAddress;
}
