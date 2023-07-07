package com.example.deukgeun.trainer.service.implement;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.global.repository.ApplicantRepository;
import com.example.deukgeun.trainer.request.SaveApplicantRequest;
import com.example.deukgeun.trainer.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;

    @Override
    public Applicant save(SaveApplicantRequest saveApplicantRequest) {
        Applicant applicant = Applicant
                .builder()
                .jobPostingId(saveApplicantRequest.getJobPostingId())
                .trainerId(saveApplicantRequest.getTrainerId())
                .supportAmount(saveApplicantRequest.getSupportAmount())
                .build();

        return applicantRepository.save(applicant);
    }
}
