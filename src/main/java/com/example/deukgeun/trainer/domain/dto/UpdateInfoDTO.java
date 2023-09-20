package com.example.deukgeun.trainer.domain.dto;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import lombok.Data;

@Data
public class UpdateInfoDTO {
    private String email;
    private String name;
    private String postcode;
    private String jibunAddress;
    private String roadAddress;
    private String detailAddress;
    private String extraAddress;
    private Integer price;
    private Gender gender;
    private GroupStatus groupStatus;
    private String groupName;
    private String introduction;
}
