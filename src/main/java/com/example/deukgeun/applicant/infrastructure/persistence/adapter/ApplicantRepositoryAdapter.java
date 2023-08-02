package com.example.deukgeun.applicant.infrastructure.persistence.adapter;

import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
import com.example.deukgeun.applicant.domain.repository.ApplicantRepository;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.MatchInfoEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.repository.ApplicantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApplicantRepositoryAdapter implements ApplicantRepository {
    private final ApplicantJpaRepository applicantJpaRepository;

    @Override
    public boolean existsByJobPostingIdAndMatchInfoIdNotNull(Long jobPostingId) {
        return applicantJpaRepository.existsByJobPostingIdAndMatchInfoIdNotNull(jobPostingId);
    }

    @Override
    public boolean existsByJobPostingIdAndTrainerId(Long jobPositingId, Long trainerId) {
        return applicantJpaRepository.existsByJobPostingIdAndTrainerId(jobPositingId, trainerId);
    }

    @Override
    public Optional<Applicant> findById(Long id) {
        Optional<ApplicantEntity> applicantEntity = applicantJpaRepository.findById(id);
        return applicantEntity.map(this::convert);
    }


    @Override
    public Page<Applicant> findPageByJobPostingId(Long jobPostingId, PageRequest pageRequest) {
        Page<ApplicantEntity> applicantEntities = applicantJpaRepository.findPageByJobPostingId(jobPostingId, pageRequest);
        return applicantEntities.map(this::convert);
    }

    @Override
    public Applicant save(Applicant applicant) {
        ApplicantEntity applicantEntity = applicantJpaRepository.save(convert(applicant));
        return convert(applicantEntity);
    }

    private Applicant convert(ApplicantEntity applicantEntity) {
        return new Applicant(
                applicantEntity.getId(),
                applicantEntity.getJobPostingId(),
                applicantEntity.getMatchInfoId(),
                applicantEntity.getTrainerId(),
                applicantEntity.getSupportAmount(),
                applicantEntity.getIsSelected(),
                applicantEntity.getJobPosting(),
                convert(applicantEntity.getMatchInfoEntity())
        );
    }

    private ApplicantEntity convert(Applicant applicant) {
        return ApplicantEntity
                .builder()
                .id(applicant.getId())
                .jobPostingId(applicant.getJobPostingId())
                .trainerId(applicant.getTrainerId())
                .supportAmount(applicant.getSupportAmount())
                .isSelected(applicant.getIsSelected())
                .jobPosting(applicant.getJobPosting())
                .matchInfoEntity(convert(applicant.getMatchInfo()))
                .matchInfoId(applicant.getMatchInfoId())
                .build();
    }

    private MatchInfo convert(MatchInfoEntity matchInfoEntity) {
        if (matchInfoEntity == null) {
            return null;
        }
        return new MatchInfo(
                matchInfoEntity.getId(),
                matchInfoEntity.getJobPostingId(),
                matchInfoEntity.getApplicantId(),
                matchInfoEntity.getStatus()
        );
    }

    private MatchInfoEntity convert(MatchInfo matchInfo) {
        if (matchInfo == null) {
            return null;
        }
        return MatchInfoEntity
                .builder()
                .id(matchInfo.getId())
                .applicantId(matchInfo.getApplicantId())
                .jobPostingId(matchInfo.getJobPostingId())
                .status(matchInfo.getStatus())
                .build();
    }
}
