package com.example.deukgeun.trainer.request;

import com.example.deukgeun.trainer.validator.ValidFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SaveProfileRequest {

    @NotNull(message = "트레이너 아이디는 필수 입력 값입니다.")
    private Long userId;

    @ValidFile
    private MultipartFile profile;
}
