package com.example.deukgeun.member.controller;

import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.member.request.SaveMatchInfoRequest;
import com.example.deukgeun.member.service.ApplicantService;
import com.example.deukgeun.member.service.JobPostingService;
import com.example.deukgeun.member.service.MatchInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController("member.controller.matchInfo")
@RequestMapping("/api/member/matchInfo")
@RequiredArgsConstructor
public class MatchInfoController {

    private final MatchInfoService matchInfoService;
    private final JobPostingService jobPostingService;
    private final ApplicantService applicantService;

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> select(@Valid SaveMatchInfoRequest saveMatchInfoRequest, BindingResult bindingResult) {
        matchInfoService.save(saveMatchInfoRequest);
        applicantService.updateIsSelectedByApplicantId(saveMatchInfoRequest.getApplicantId(), 1);
        jobPostingService.updateIsActiveByJobPostingId(2, saveMatchInfoRequest.getJobPostingId());

        return RestResponseUtil.ok("매칭 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> cancel(@RequestParam @NotBlank(message = "applicantId is required")Long applicantId) {
        matchInfoService.deleteByApplicantId(applicantId);
        applicantService.updateIsSelectedByApplicantId(applicantId, 0);

        return RestResponseUtil.ok("취소 성공했습니다.", null);
    }

}
