package com.example.deukgeun.applicant.infrastructure.persistence.repository;

import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantJpaRepository extends JpaRepository<ApplicantEntity, Long> {
    boolean existsByJobPostingIdAndMatchInfoIdNotNull(Long id);
    Page<ApplicantEntity> findPageByJobPostingId(Long jobPostingId, PageRequest pageRequest);

    boolean existsByJobPostingIdAndTrainerId(Long jobPostingId, Long trainerId);

}
