package com.example.deukgeun.job.application.service.implement;

import com.example.deukgeun.job.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.job.application.dto.response.ApplicantResponse;
import com.example.deukgeun.job.domain.entity.Applicant;
import com.example.deukgeun.job.domain.repository.ApplicantRepository;
import com.example.deukgeun.job.domain.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
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
    public Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId) {
        if (applicantRepository.existsByJobPostingIdAndTrainerId(saveApplicantRequest.getJobPostingId(), trainerId)) {
            throw new EntityExistsException("이미 지원한 공고 입니다.");
        }

        Applicant applicant = Applicant
                .builder()
                .jobPostingId(saveApplicantRequest.getJobPostingId())
                .trainerId(trainerId)
                .supportAmount(saveApplicantRequest.getSupportAmount())
                .build();

        return applicantRepository.save(applicant);
    }

    @Override
    public void updateIsSelectedByApplicantId(Long applicantId, int isSelected) {
        Applicant applicant = applicantRepository.findById(applicantId).
                orElseThrow(() -> new EntityNotFoundException("찾을수 없는 정보입니다."));

        applicant.updateIsSelect(isSelected);
        applicantRepository.save(applicant);
    }

    @Override
    public ApplicantResponse.ApplicantInfo getById(Long id) {
        Applicant applicant = applicantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("없는 지원 정보입니다.")
        );

        return new ApplicantResponse.ApplicantInfo(applicant);
    }
}
