package com.example.deukgeun.trainer.infrastructure.persistence.model.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressVo {
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
