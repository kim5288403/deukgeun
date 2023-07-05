package com.example.deukgeun.member.controller;

import com.example.deukgeun.commom.request.LoginRequest;
import com.example.deukgeun.commom.response.LoginResponse;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.member.entity.Member;
import com.example.deukgeun.member.request.JoinRequest;
import com.example.deukgeun.member.service.implement.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberService;
    private final TokenServiceImpl tokenService;

    @Value("${deukgeun.role.member}")
    private String role;

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid JoinRequest request, BindingResult bindingResult) {
        Member saveMember = memberService.save(request);

        return RestResponseUtil.ok("회원 가입 성공 했습니다.", saveMember);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(@Valid LoginRequest request, BindingResult bindingResult, HttpServletResponse response) {
        // 사용자 로그인 처리
        Member member = memberService.getByEmail(request.getEmail());
        memberService.isPasswordMatches(request.getPassword(), member);

        // JWT 토큰 생성 및 설정
        String authToken = tokenService.setToken(request.getEmail(), response, role);

        // 로그인 응답 객체 생성
        LoginResponse loginResponse = LoginResponse
                .builder()
                .authToken(authToken)
                .role(role)
                .build();

        return RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);
    }

}
