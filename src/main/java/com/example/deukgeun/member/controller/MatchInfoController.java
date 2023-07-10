package com.example.deukgeun.member.controller;

import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.member.request.SaveMatchInfoRequest;
import com.example.deukgeun.member.service.JobPostingService;
import com.example.deukgeun.member.service.MatchInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("member.controller.matchInfo")
@RequestMapping("/api/member/matchInfo")
@RequiredArgsConstructor
public class MatchInfoController {

    private final MatchInfoService matchInfoService;
    private final JobPostingService jobPostingService;

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid SaveMatchInfoRequest saveMatchInfoRequest, BindingResult bindingResult) {
        matchInfoService.save(saveMatchInfoRequest);
        jobPostingService.updateIsActiveByJobPostingId(2, saveMatchInfoRequest.getJobPostingId());

        return RestResponseUtil.ok("매칭 성공했습니다.", null);
    }

}
