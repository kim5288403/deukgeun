package com.example.deukgeun.member.domain.dto;

import com.example.deukgeun.global.enums.Gender;
import lombok.Data;

@Data
public class MemberJoinDTO {
    private String name;
    private Integer age;
    private String email;
    private String password;
    private Gender gender;
}
