package com.example.deukgeun.trainer.application.dto.request.validator;

import com.example.deukgeun.trainer.application.service.implement.ProfileApplicationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class FileTypeValidator implements ConstraintValidator<ValidFileType, MultipartFile> {

    private final ProfileApplicationServiceImpl profileApplicationService;

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return profileApplicationService.isSupportedContentType(value);
    }
}
