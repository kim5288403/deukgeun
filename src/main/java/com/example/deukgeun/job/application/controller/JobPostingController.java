package com.example.deukgeun.job.application.controller;

import com.example.deukgeun.job.domain.entity.JobPosting;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.auth.application.dto.response.JobPostingResponse;
import com.example.deukgeun.job.domain.service.JobPostingService;
import com.example.deukgeun.auth.application.service.implement.TokenServiceImpl;
import com.example.deukgeun.job.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.member.infrastructure.persistence.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController("main.jobPosting.controller")
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final MemberServiceImpl memberService;
    private final TokenServiceImpl tokenService;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> list(String keyword, int currentPage) {
        Page<JobPostingResponse.ListResponse> list = jobPostingService.getListByKeyword(keyword, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/member")
    public ResponseEntity<?> listByMemberId(HttpServletRequest request, int currentPage) {
        String token = tokenService.resolveAuthToken(request);
        String userPk = tokenService.getUserPk(token);
        Member member = memberService.getByEmail(userPk);
        Page<JobPostingResponse.ListResponse> list = jobPostingService.getByMemberId(member.getId(), currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveJobPostingRequest saveJobPostingRequest, BindingResult bindingResult) {
        String token = tokenService.resolveAuthToken(request);
        String userPk = tokenService.getUserPk(token);
        Member member = memberService.getByEmail(userPk);
        jobPostingService.save(saveJobPostingRequest, member.getId());

        return RestResponseUtil.ok("등록 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        JobPosting detail = jobPostingService.getById(id);

        return RestResponseUtil.ok("조회 성공했습니다.", detail);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/check")
    public ResponseEntity<?> checkJobPostingOwnership(HttpServletRequest request, Long id) {
        String authToken = tokenService.resolveAuthToken(request);
        String email = tokenService.getUserPk(authToken);
        Member member = memberService.getByEmail(email);
        boolean result = jobPostingService.existsByIdAndMemberId(id, member.getId());

        return RestResponseUtil.ok("체크 성공했습니다.", result);
    }
}
