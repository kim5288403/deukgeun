package com.example.deukgeun.trainer.valid;

import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RequestValidExceptionHandlingTest {

    @Mock
    private BindingResult bindingResult;

    @Autowired
    private ValidateServiceImpl validateService;

    @Test
    void shouldThrowRequestValidExceptionForValidErrors() {
        // Given
        Map<String, String> errorMap = new HashMap<>();
        given(bindingResult.hasErrors()).willReturn(true);

        // When, Then
        assertThrows(RequestValidException.class, () -> {
            validateService.requestValidExceptionHandling(bindingResult);
        });
    }

    @Test
    void shouldNotThrowRequestValidExceptionForEmptyErrors() {
        // Given
        Map<String, String> errorMap = new HashMap<>();
        given(bindingResult.hasErrors()).willReturn(false);

        // When, Then
        assertDoesNotThrow( () -> {
            if (bindingResult.hasErrors()) {
                validateService.requestValidExceptionHandling(bindingResult);
            }
        });
    }

    @Test
    void shouldReturnValidatorResultForFieldErrors() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("objectName", "field", "Error message default");
        FieldError fieldError2 = new FieldError("objectName", "profile", "Error message profile");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // When
        Map<String, String> result = validateService.fieldErrorsMessageHandling(bindingResult);

        // Then
        assertEquals(2, result.size());
        assertEquals("Error message default", result.get("valid_field"));
        assertEquals("Error message profile", result.get("valid_profile"));
    }

    @Test
    void shouldReturnEmptyValidatorResultForEmptyFieldErrors() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);

        // When
        Map<String, String> result = validateService.fieldErrorsMessageHandling(bindingResult);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnValidatorResultForGlobalErrors() {
        // Given
        Map<String, String> validatorResult = new HashMap<>();
        BindingResult bindingResult = mock(BindingResult.class);
        ObjectError GloBalError1 = new ObjectError("objectName1", "Global Error message1");
        ObjectError GloBalError2 = new ObjectError("objectName2", "Global Error message2");
        when(bindingResult.getAllErrors()).thenReturn(List.of(GloBalError1, GloBalError2));

        // When
        Map<String, String> result = validateService.globalErrorsMessageHandling(validatorResult, bindingResult);

        // Then
        assertEquals(2, result.size());
        assertEquals("Global Error message1", result.get("valid_objectName1"));
        assertEquals("Global Error message2", result.get("valid_objectName2"));
    }

    @Test
    void shouldReturnEmptyValidatorResultForEmptyGlobalErrors() {
        // Given
        Map<String, String> validatorResult = new HashMap<>();
        BindingResult bindingResult = mock(BindingResult.class);

        // When
        Map<String, String> result = validateService.globalErrorsMessageHandling(validatorResult, bindingResult);

        // Then
        assertEquals(0, result.size());
    }


}
