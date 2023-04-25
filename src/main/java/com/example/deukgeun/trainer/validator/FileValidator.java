package com.example.deukgeun.trainer.validator;

import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private final ProfileServiceImpl profileService;

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        boolean flag = true;
        if (value == null) {
            flag = false;
        } else {

            if (value.isEmpty()) {
                flag = false;
            }

            if (!profileService.isSupportedContentType(value.getContentType())) {
                flag = false;
            }
        }

        return flag;
    }
}
