package com.example.deukgeun.member.domain.entity;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class Member {

    private Long id;

    private String email;

    private String password;

    private String name;

    private Integer age;

    private Gender gender;

    public Member(
            Long id,
            String email,
            String password,
            String name,
            Integer age,
            Gender gender
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public static Member create (
            String email,
            String password,
            String name,
            Integer age,
            Gender gender
    ) {
        Long id = LongIdGeneratorUtil.gen();
        return new Member(id, email, password, name, age, gender);
    }
}