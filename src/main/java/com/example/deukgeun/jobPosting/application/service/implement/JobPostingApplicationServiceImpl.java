package com.example.deukgeun.jobPosting.application.service.implement;

import com.example.deukgeun.jobPosting.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.jobPosting.application.dto.response.JobPostingResponse;
import com.example.deukgeun.jobPosting.application.service.JobPostingApplicationService;
import com.example.deukgeun.jobPosting.domain.model.aggregate.JobPosting;
import com.example.deukgeun.jobPosting.domain.service.JobPostingDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobPostingApplicationServiceImpl implements JobPostingApplicationService {

    private final JobPostingDomainService jobPostingDomainService;

    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobPostingDomainService.existsByIdAndMemberId(id, memberId);
    }

    @Override
    public JobPosting findById(Long id) {
        return jobPostingDomainService.findById(id);
    }

    @Override
    public Page<JobPostingResponse.List> getListByKeyword(String keyword, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        String likeKeyword = "%" + keyword + "%";
        Page<JobPosting> jobPostings = jobPostingDomainService.getListByKeyword(likeKeyword, pageRequest);

        return jobPostings.map(JobPostingResponse.List::new);
    }

    @Override
    public Page<JobPostingResponse.List> getListByMemberId(Long memberId, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        Page<JobPosting> jobPostings = jobPostingDomainService.getListByMemberId(memberId, pageRequest);

        return jobPostings.map(JobPostingResponse.List::new);
    }

    @Override
    public JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId) {
        return jobPostingDomainService.save(saveJobPostingRequest, memberId);
    }

    @Override
    public void updateIsActiveByJobPostingId(int isActive, Long id) {
        jobPostingDomainService.updateIsActiveByJobPostingId(isActive, id);
    }
}
