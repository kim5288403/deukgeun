package com.example.deukgeun.commom.controller;

import com.example.deukgeun.commom.response.JobPostingResponse;
import com.example.deukgeun.commom.service.JobPostingService;
import com.example.deukgeun.commom.util.RestResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("common.jobPosting.controller")
@RequestMapping("/api/jobPosting")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> list(String keyword, int currentPage) {
        Page<JobPostingResponse.ListResponse> list = jobPostingService.getList(keyword, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }
}
