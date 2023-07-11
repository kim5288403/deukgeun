package com.example.deukgeun.member.service.implement;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.global.repository.ApplicantRepository;
import com.example.deukgeun.member.response.ApplicantResponse;
import com.example.deukgeun.member.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service("member.service.applicant")
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {
    private final ApplicantRepository applicantRepository;

    @Override
    public Page<ApplicantResponse.ListResponse> getByJobPostingId(Long jobPostingId, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        return applicantRepository.findByJobPostingId(jobPostingId, pageRequest);
    }

    @Override
    public void updateIsSelectedByApplicantId(Long applicantId, int isSelected) {
        Applicant applicant = applicantRepository.findById(applicantId).
                orElseThrow(() -> new EntityNotFoundException("찾을수 없는 정보입니다."));

        applicant.updateIsSelect(isSelected);
        applicantRepository.save(applicant);
    }
}
