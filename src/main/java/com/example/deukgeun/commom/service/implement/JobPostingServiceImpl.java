package com.example.deukgeun.commom.service.implement;

import com.example.deukgeun.commom.repository.JobPostingRepository;
import com.example.deukgeun.commom.response.JobPostingResponse;
import com.example.deukgeun.commom.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service("common.jobPosting.service")
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    @Override
    public Page<JobPostingResponse.ListResponse> getList(String keyword, int currentPage) {
        String likeKeyword = "%" + keyword + "%";
        PageRequest pageable = PageRequest.of(currentPage, 10);

        return jobPostingRepository.findByLikeKeyword(likeKeyword, pageable);
    }
}
