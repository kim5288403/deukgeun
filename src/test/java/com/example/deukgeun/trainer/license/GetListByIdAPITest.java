package com.example.deukgeun.trainer.license;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetListByIdAPITest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetListByIdForValidRequest() throws Exception {
        // Given
        long id = 9999;
        // When
        mockMvc.perform(get("/api/trainer/license/" + id)
                )
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("자격증 조회 성공했습니다."));
    }

    @Test
    void shouldMethodArgumentTypeMismatchExceptionForInvalidRequest() throws Exception {
        // Given
        String id = "invalid";
        // When
        mockMvc.perform(get("/api/trainer/license/" + id)
                )
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }

}
