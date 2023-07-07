package com.example.deukgeun.main.controller;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.main.response.JobPostingResponse;
import com.example.deukgeun.main.service.JobPostingService;
import com.example.deukgeun.global.util.RestResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("common.jobPosting.controller")
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> list(String keyword, int currentPage) {
        Page<JobPostingResponse.ListResponse> list = jobPostingService.getList(keyword, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        JobPosting detail = jobPostingService.getById(id);

        return RestResponseUtil.ok("조회 성공했습니다.", detail);
    }
}
