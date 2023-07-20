package com.example.deukgeun.job.application.controller;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.job.application.dto.response.JobPostingResponse;
import com.example.deukgeun.job.application.service.JobPostingService;
import com.example.deukgeun.job.domain.entity.JobPosting;
import com.example.deukgeun.member.application.service.implement.MemberApplicationServiceImpl;
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

@RestController("main.jobPosting.controller")
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final MemberApplicationServiceImpl memberApplicationService;
    private final AuthTokenApplicationServiceImpl authTokenApplicationService;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> list(String keyword, int currentPage) {
        Page<JobPostingResponse.ListResponse> list = jobPostingService.getListByKeyword(keyword, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/member")
    public ResponseEntity<?> listByMemberId(HttpServletRequest request, int currentPage) {
        String token = authTokenApplicationService.resolveAuthToken(request);
        String userPk = authTokenApplicationService.getUserPk(token);
        Member member = memberApplicationService.findByEmail(userPk);
        Page<JobPostingResponse.ListResponse> list = jobPostingService.getByMemberId(member.getId(), currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveJobPostingRequest saveJobPostingRequest, BindingResult bindingResult) {
        String token = authTokenApplicationService.resolveAuthToken(request);
        String userPk = authTokenApplicationService.getUserPk(token);
        Member member = memberApplicationService.findByEmail(userPk);
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
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Member member = memberApplicationService.findByEmail(email);
        boolean result = jobPostingService.existsByIdAndMemberId(id, member.getId());

        return RestResponseUtil.ok("체크 성공했습니다.", result);
    }
}
