package com.example.deukgeun.job.infrastructure.persistence.repository;


import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobJpaRepository extends JpaRepository<JobEntity, Long> {

    @Query("select j from JobEntity j where j.title like :keyword or j.jobAddressVo.jibunAddress like :keyword or j.jobAddressVo.extraAddress like :keyword or j.jobAddressVo.detailAddress like :keyword or j.jobAddressVo.roadAddress like :keyword and j.isActive = 1")
    Page<JobEntity> findByLikeKeyword(@Param(value = "keyword")String keyword, PageRequest pageRequest);

    Page<JobEntity> findByMemberId(Long memberId, PageRequest pageRequest);

    boolean existsByIdAndMemberId(Long id, Long memberId);

}
