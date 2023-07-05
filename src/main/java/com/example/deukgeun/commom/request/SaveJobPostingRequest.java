package com.example.deukgeun.commom.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SaveJobPostingRequest {

    @NotBlank(message = "이름 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "트레이너 요구사항 및 자격조건은 필수 입력 값입니다.")
    private Integer requirements;

    @NotBlank(message = "우편번호는 필수 입력 값입니다.")
    private String postcode;

    private String jibunAddress;

    private String roadAddress;

    private String detailAddress;

    private String extraAddress;

    @NotNull(message = "시작일은 필수 입력 값입니다.")
    private LocalDateTime startDate;

    @NotNull(message = "종료일은 필수 입력 값입니다.")
    private LocalDateTime endDate;
}
