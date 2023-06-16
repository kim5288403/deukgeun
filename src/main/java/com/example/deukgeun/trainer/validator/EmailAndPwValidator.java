package com.example.deukgeun.trainer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.deukgeun.trainer.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailAndPwValidator implements ConstraintValidator<ValidEmailAndPw, Object> {

    private final ValidateServiceImpl validateService;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        String email = validateService.getFieldValue(object, "email");
        String password = validateService.getFieldValue(object, "password");
        try {
            Member user = userService.getByEmail(email);
            return passwordEncoder.matches(password, user.getPassword());
        } catch (Exception e) {
            return false;
        }
    }

}
