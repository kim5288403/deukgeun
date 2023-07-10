package com.example.deukgeun.trainer.service.implement;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.global.repository.ApplicantRepository;
import com.example.deukgeun.trainer.request.SaveApplicantRequest;
import com.example.deukgeun.trainer.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;

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
}
