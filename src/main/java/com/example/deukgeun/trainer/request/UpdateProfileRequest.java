package com.example.deukgeun.trainer.request;

import com.example.deukgeun.trainer.validator.ValidFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {

    @ValidFile
    private MultipartFile profile;
}
