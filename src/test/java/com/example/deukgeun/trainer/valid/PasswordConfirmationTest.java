package com.example.deukgeun.trainer.valid;

import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.validator.PasswordConfirmValidator;
import com.example.deukgeun.trainer.request.JoinRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordConfirmationTest {

    @Autowired
    private ValidateServiceImpl validateService;

    @Mock
    private ConstraintValidatorContext context;

    @Test
    void shouldReturnTrueForValidPasswords() {
        // Given
        String password = "test1";
        String confirm = "test1";
        JoinRequest object = new JoinRequest();
        object.setPassword(password);
        object.setPasswordConfirm(confirm);
        PasswordConfirmValidator passwordConfirmValidator = new PasswordConfirmValidator(validateService);

        // When
        boolean result = passwordConfirmValidator.isValid(object, context);

        // Then
        assertTrue(result);
    }

}
