package com.example.deukgeun.jobPosting.domain.model.valueobject;

import lombok.Getter;

@Getter
public class JobAddress {
    private String postcode;

    private String jibunAddress;

    private String roadAddress;

    private String detailAddress;

    private String extraAddress;

    public JobAddress(
            String postcode,
            String jibunAddress,
            String roadAddress,
            String detailAddress,
            String extraAddress) {

        this.postcode = postcode;
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }
}
