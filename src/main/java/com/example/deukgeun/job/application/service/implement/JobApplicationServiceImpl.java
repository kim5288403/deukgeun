package com.example.deukgeun.job.application.service.implement;

import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.application.dto.response.JobResponse;
import com.example.deukgeun.job.application.service.JobApplicationService;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.service.JobDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobDomainService jobDomainService;

    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobDomainService.existsByIdAndMemberId(id, memberId);
    }

    @Override
    public Job findById(Long id) {
        return jobDomainService.findById(id);
    }

    @Override
    public Page<JobResponse.List> getListByKeyword(String keyword, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        String likeKeyword = "%" + keyword + "%";
        Page<Job> job = jobDomainService.getListByKeyword(likeKeyword, pageRequest);

        return job.map(JobResponse.List::new);
    }

    @Override
    public Page<JobResponse.List> getListByMemberId(Long memberId, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        Page<Job> job = jobDomainService.getListByMemberId(memberId, pageRequest);

        return job.map(JobResponse.List::new);
    }

    @Override
    public Job save(SaveJobRequest saveJobRequest, Long memberId) {
        return jobDomainService.save(saveJobRequest, memberId);
    }

    @Override
    public void updateIsActiveByJobId(int isActive, Long id) {
        jobDomainService.updateIsActiveByJobId(isActive, id);
    }
}
