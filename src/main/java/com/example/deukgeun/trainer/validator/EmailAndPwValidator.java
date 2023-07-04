package com.example.deukgeun.trainer.validator;

import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.trainer.entity.Trainer;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailAndPwValidator implements ConstraintValidator<ValidEmailAndPw, Object> {

    private final ValidateServiceImpl validateService;
    private final TrainerServiceImpl trainerService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        String email = validateService.getFieldValue(object, "email");
        String password = validateService.getFieldValue(object, "password");
        try {
            Trainer trainer = trainerService.getByEmail(email);
            return passwordEncoder.matches(password, trainer.getPassword());
        } catch (Exception e) {
            return false;
        }
    }

}
