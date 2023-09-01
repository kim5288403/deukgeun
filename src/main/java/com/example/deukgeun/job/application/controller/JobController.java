package com.example.deukgeun.job.application.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.application.dto.response.JobResponse;
import com.example.deukgeun.job.application.service.JobApplicationService;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.application.service.MemberApplicationService;
import com.example.deukgeun.member.domain.entity.Member;
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

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobApplicationService jobApplicationService;
    private final MemberApplicationService memberApplicationService;
    private final AuthTokenApplicationService authTokenApplicationService;

    @RequestMapping(method = RequestMethod.GET, path = "/check")
    public ResponseEntity<?> checkJobOwnership(HttpServletRequest request, Long id) {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Member member = memberApplicationService.findByEmail(email);
        boolean result = jobApplicationService.existsByIdAndMemberId(id, member.getId());

        return RestResponseUtil.ok("체크 성공했습니다.", result);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        Job detail = jobApplicationService.findById(id);

        return RestResponseUtil.ok("조회 성공했습니다.", detail);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getListByKeyword(String keyword, int currentPage) {
        Page<JobResponse.List> list = jobApplicationService.getListByKeyword(keyword, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/member")
    public ResponseEntity<?> getListByMemberId(HttpServletRequest request, int currentPage) {
        String token = authTokenApplicationService.resolveAuthToken(request);
        String userPk = authTokenApplicationService.getUserPk(token);
        Member member = memberApplicationService.findByEmail(userPk);
        Page<JobResponse.List> list = jobApplicationService.getListByMemberId(member.getId(), currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveJobRequest saveJobRequest, BindingResult bindingResult) {
        String token = authTokenApplicationService.resolveAuthToken(request);
        String userPk = authTokenApplicationService.getUserPk(token);
        Member member = memberApplicationService.findByEmail(userPk);
        jobApplicationService.save(saveJobRequest, member.getId());

        return RestResponseUtil.ok("등록 성공했습니다.", null);
    }
}
