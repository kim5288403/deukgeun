package com.example.deukgeun.applicant.application.controller;

import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.service.JobPostingApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final ApplicantApplicationService applicantApplicationService;
    private final JobPostingApplicationService jobPostingApplicationService;

    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> cancel(@RequestParam @NotBlank(message = "id is required")Long id) {
        applicantApplicationService.deleteMatchInfoById(id);
        applicantApplicationService.updateIsSelectedById(id, 0);

        return RestResponseUtil.ok("취소 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/check/{id}")
    public ResponseEntity<?> isAnnouncementMatched(@PathVariable Long id) {
        applicantApplicationService.isAnnouncementMatchedByJobPostingId(id);

        return RestResponseUtil.ok("검사 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> matching(@Valid SaveMatchInfoRequest saveMatchInfoRequest, BindingResult bindingResult) {
        applicantApplicationService.matching(saveMatchInfoRequest);
        applicantApplicationService.updateIsSelectedById(saveMatchInfoRequest.getApplicantId(), 1);
        jobPostingApplicationService.updateIsActiveByJobPostingId(2, saveMatchInfoRequest.getJobPostingId());

        return RestResponseUtil.ok("매칭 성공했습니다.", null);
    }
}
