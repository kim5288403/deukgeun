package com.example.deukgeun.common.jwt;

import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SetHeaderRoleTest {
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private HttpServletResponse response;

    @Test
    void shouldSetHeaderRoleForValidParameter() {
        // Given
        String role = "testRole";

        // When
        jwtService.setHeaderRole(response, role);
        String result = response.getHeader("role");

        // Then
        assertEquals(role, result);
    }
}
