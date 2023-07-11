package com.example.deukgeun.main.service.implement;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.repository.JobPostingRepository;
import com.example.deukgeun.main.response.JobPostingResponse;
import com.example.deukgeun.main.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service("main.jobPosting.service")
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    @Override
    public Page<JobPostingResponse.ListResponse> getList(String keyword, int currentPage) {
        String likeKeyword = "%" + keyword + "%";
        PageRequest pageable = PageRequest.of(currentPage, 10);

        return jobPostingRepository.findByLikeKeyword(likeKeyword, pageable);
    }

    @Override
    public JobPosting getById(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 공고 정보 입니다."));
    }

    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobPostingRepository.existsByIdAndMemberId(id, memberId);
    }
}
