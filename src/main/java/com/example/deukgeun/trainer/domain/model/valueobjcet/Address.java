package com.example.deukgeun.trainer.domain.model.valueobjcet;

import lombok.Getter;

@Getter
public class Address {
    private String postcode;
    private String jibunAddress;
    private String roadAddress;
    private String detailAddress;
    private String extraAddress;

    public Address(
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
