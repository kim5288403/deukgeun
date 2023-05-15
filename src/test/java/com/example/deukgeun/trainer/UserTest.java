package com.example.deukgeun.trainer;

import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void login() throws Exception {
        String email = "email";
        userService.login(email);
    }
}
