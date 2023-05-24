package com.example.deukgeun.trainer.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetListAPITest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetListAPIForValidParam() throws Exception {
        // Given
        String keyword = "김";
        String currentPage = "1";

        // When
        mockMvc.perform(get("/api/trainer/")
                        .param("keyword", keyword)
                        .param("currentPage", currentPage)
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("조회 성공 했습니다."));
    }

    @Test
    void shouldGetListAPIForInvalidParam() throws Exception {
        // When
        mockMvc.perform(get("/api/trainer/")
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("조회 성공 했습니다."));
    }

    @Test
    void shouldBADREQUESTForInvalidParam() throws Exception {
        // When
        mockMvc.perform(get("/api/trainer/")
                        .param("currentPage", "123412341234")
                )
                // then
                .andExpect(status().isBadRequest());
    }

}
