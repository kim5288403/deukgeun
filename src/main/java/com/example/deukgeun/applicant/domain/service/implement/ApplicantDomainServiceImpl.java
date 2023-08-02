package com.example.deukgeun.applicant.domain.service.implement;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
import com.example.deukgeun.applicant.domain.repository.ApplicantRepository;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ApplicantDomainServiceImpl implements ApplicantDomainService {
    private final ApplicantRepository applicantRepository;

    @Override
    public void deleteMatchInfoById(Long id) {
        Applicant applicant = findById(id);
        applicant.deleteMatchInfo();
        applicantRepository.save(applicant);
    }

    @Override
    public Applicant findById(Long id) {
        return applicantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("없는 지원 정보입니다.")
        );
    }

    @Override
    public Page<Applicant> getByJobPostingId(Long jobPostingId, PageRequest pageRequest) {
        return applicantRepository.findPageByJobPostingId(jobPostingId, pageRequest);
    }

    @Override
    public boolean isAnnouncementMatchedByJobPostingId(Long jobPostingId) {
        return applicantRepository.existsByJobPostingIdAndMatchInfoIdNotNull(jobPostingId);
    }

    @Override
    public Applicant matching(SaveMatchInfoRequest saveMatchInfoRequest, int status) {
        Applicant applicant = findById(saveMatchInfoRequest.getApplicantId());

        MatchInfo matchInfo = MatchInfo.create(
                saveMatchInfoRequest.getJobPostingId(),
                saveMatchInfoRequest.getApplicantId(),
                status
        );

        applicant.setMatchInfo(matchInfo);
        System.out.println(applicant.getJobPostingId());

        return applicantRepository.save(applicant);
    }

    @Override
    public Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId) {
        if (applicantRepository.existsByJobPostingIdAndTrainerId(saveApplicantRequest.getJobPostingId(), trainerId)) {
            throw new EntityExistsException("이미 지원한 공고 입니다.");
        }

        Applicant applicant = Applicant.create(
                saveApplicantRequest.getJobPostingId(),
                trainerId,
                saveApplicantRequest.getSupportAmount(),
                0
        );

        return applicantRepository.save(applicant);
    }

    @Override
    public void updateIsSelectedById(Long applicantId, int isSelected) {
        Applicant applicant = applicantRepository.findById(applicantId).
                orElseThrow(() -> new EntityNotFoundException("찾을수 없는 정보입니다."));

        applicant.updateIsSelect(isSelected);
        applicantRepository.save(applicant);
    }
}
