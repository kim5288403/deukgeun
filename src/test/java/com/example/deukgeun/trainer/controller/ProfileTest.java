package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.trainer.service.implement.MemberServiceImpl;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProfileTest {

    @InjectMocks
    private ProfileController profileController;
    @Mock
    private ProfileServiceImpl profileService;
    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private TokenServiceImpl tokenService;
}
