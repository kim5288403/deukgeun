package com.example.deukgeun.trainer.request;

import com.example.deukgeun.trainer.validator.ValidFileType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {

    @ValidFileType
    private MultipartFile profile;
}
