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

    /**
     * 새로운 회원을 가입시킵니다.
     *
     * @param request        가입 요청 정보를 포함하는 객체입니다.
     * @param bindingResult  입력 유효성 검사 결과를 담은 객체입니다.
     * @return 회원 가입 성공 여부를 나타내는 응답입니다.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid JoinRequest request, BindingResult bindingResult) {
        memberApplicationService.save(request);

        return RestResponseUtil.ok("회원 가입 성공 했습니다.", null);
    }
}
