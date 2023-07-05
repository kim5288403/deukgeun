package com.example.deukgeun.member.controller;

import com.example.deukgeun.commom.entity.JobPosting;
import com.example.deukgeun.commom.request.SaveJobPostingRequest;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.member.entity.Member;
import com.example.deukgeun.member.request.JoinRequest;
import com.example.deukgeun.member.service.JobPostingService;
import com.example.deukgeun.member.service.implement.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/member/job")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final TokenServiceImpl tokenService;
    private final MemberServiceImpl memberService;

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveJobPostingRequest saveJobPostingRequest, BindingResult bindingResult) {
        String token = tokenService.resolveAuthToken(request);
        String userPk = tokenService.getUserPk(token);
        Member member = memberService.getByEmail(userPk);
        jobPostingService.save(saveJobPostingRequest, member.getId());

        return RestResponseUtil.ok("등록 성공했습니다.", null);
    }
}
