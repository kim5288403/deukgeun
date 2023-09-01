package com.example.deukgeun.applicant.infrastructure.persistence.repository;

import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantJpaRepository extends JpaRepository<ApplicantEntity, Long> {
    boolean existsByJobIdAndMatchInfoIdNotNull(Long id);
    Page<ApplicantEntity> findPageByJobId(Long jobId, PageRequest pageRequest);

    boolean existsByJobIdAndTrainerId(Long jobId, Long trainerId);

}
