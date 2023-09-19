package com.example.deukgeun.member.domain.aggregate;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {

    private Long id;

    private String email;

    private String password;

    private String name;

    private Integer age;

    private Gender gender;

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