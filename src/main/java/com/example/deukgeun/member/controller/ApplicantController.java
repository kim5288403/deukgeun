package com.example.deukgeun.member.controller;

import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.member.response.ApplicantResponse;
import com.example.deukgeun.member.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("member.controller.applicant")
@RequestMapping("/api/member/applicant")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> list(Long jobPostingId, int currentPage) {
        Page<ApplicantResponse.ListResponse> list = applicantService.getByJobPostingId(jobPostingId, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }
}
