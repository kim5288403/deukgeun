package com.example.deukgeun.trainer.domain.model.valueobjcet;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {
    @Column(length = 50, nullable = false)
    private String postcode;

    @Column(length = 50)
    private String jibunAddress;

    @Column(length = 50)
    private String roadAddress;

    @Column(length = 50, nullable = false)
    private String detailAddress;

    @Column(length = 50)
    private String extraAddress;
}
