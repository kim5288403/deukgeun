package com.example.deukgeun.trainer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.response.UserResponse.UserListResponse;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  
  Optional<Profile> findByUserId(Long userId);
  
  @Query("select p from Profile p where p.user.name like :keyword or p.user.groupName like :keyword or p.user.jibunAddress like :keyword or p.user.roadAddress like :keyword or p.user.detailAddress like :keyword or p.user.extraAddress like :keyword")
  Page<UserListResponse> findByUserLikeKeyword(@Param(value = "keyword")String keyword, Pageable pageable);

  @Modifying
  @Transactional
  @Query(value = "update trainer_user_profile m set m.path = :path where m.profile_id = :profile_id", nativeQuery = true)
  int updateProfile(@Param(value = "profile_id")Long profileId, @Param(value = "path")String path);
}
