package com.example.deukgeun.member.application.controller;

import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.application.service.implement.MemberApplicationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberApplicationServiceImpl memberApplicationService;

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid JoinRequest request, BindingResult bindingResult) {
        memberApplicationService.save(request);

        return RestResponseUtil.ok("회원 가입 성공 했습니다.", null);
    }
}
