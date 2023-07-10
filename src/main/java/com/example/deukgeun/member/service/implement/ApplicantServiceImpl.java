package com.example.deukgeun.member.service.implement;

import com.example.deukgeun.global.repository.ApplicantRepository;
import com.example.deukgeun.member.response.ApplicantResponse;
import com.example.deukgeun.member.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service("member.service.applicant")
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {
    private final ApplicantRepository applicantRepository;

    @Override
    public Page<ApplicantResponse.ListResponse> getByJobPostingId(Long jobPostingId, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        return applicantRepository.findByJobPostingId(jobPostingId, pageRequest);
    }
}
