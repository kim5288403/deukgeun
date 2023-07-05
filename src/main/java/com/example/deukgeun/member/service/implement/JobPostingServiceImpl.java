package com.example.deukgeun.member.service.implement;

import com.example.deukgeun.commom.entity.JobPosting;
import com.example.deukgeun.commom.repository.JobPostingRepository;
import com.example.deukgeun.commom.request.SaveJobPostingRequest;
import com.example.deukgeun.member.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    @Override
    public JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId) {
        JobPosting jobPosting = JobPosting
                .builder()
                .memberId(memberId)
                .title(saveJobPostingRequest.getTitle())
                .postcode(saveJobPostingRequest.getPostcode())
                .startDate(saveJobPostingRequest.getStartDate())
                .endDate(saveJobPostingRequest.getEndDate())
                .build();

        return jobPostingRepository.save(jobPosting);
    }
}
