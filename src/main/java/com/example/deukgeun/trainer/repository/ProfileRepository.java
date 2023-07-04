package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.response.TrainerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  
  Optional<Profile> findByTrainerId(Long trainerId);
  
  @Query("select p from Profile p where p.trainer.name like :keyword or p.trainer.groupName like :keyword or p.trainer.jibunAddress like :keyword or p.trainer.roadAddress like :keyword or p.trainer.detailAddress like :keyword or p.trainer.extraAddress like :keyword")
  Page<TrainerResponse.TrainerListResponse> findByTrainerLikeKeyword(@Param(value = "keyword")String keyword, Pageable pageable);
}
