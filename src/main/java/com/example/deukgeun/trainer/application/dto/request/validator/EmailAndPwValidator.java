package com.example.deukgeun.trainer.application.dto.request.validator;

import com.example.deukgeun.global.util.ValidateUtil;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailAndPwValidator implements ConstraintValidator<ValidEmailAndPw, Object> {

    private final TrainerApplicationService trainerApplicationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        String email = ValidateUtil.getFieldValue(object, "email");
        String password = ValidateUtil.getFieldValue(object, "password");
        try {
            Trainer trainer = trainerApplicationService.findByEmail(email);
            return passwordEncoder.matches(password, trainer.getPassword());
        } catch (Exception e) {
            return false;
        }
    }

}
