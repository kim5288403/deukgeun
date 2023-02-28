package com.example.deukgeun.trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.example.deukgeun.trainer.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  
  @Modifying
  @Transactional
  @Query(value = "update trainer_user_profile m set m.path = :path where m.profile_id = :profile_id", nativeQuery = true)
  int updateProfile(@Param(value = "profile_id")Long profileId, @Param(value = "path")String path);
}
