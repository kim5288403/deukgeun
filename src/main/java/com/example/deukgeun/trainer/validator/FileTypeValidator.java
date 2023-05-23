package com.example.deukgeun.trainer.validator;

import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@RequiredArgsConstructor
public class FileTypeValidator implements ConstraintValidator<ValidFileType, MultipartFile> {

    private final ProfileServiceImpl profileService;

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return profileService.isSupportedContentType(value);
    }
}
