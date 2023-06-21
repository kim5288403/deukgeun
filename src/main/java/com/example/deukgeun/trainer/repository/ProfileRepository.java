package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.response.UserResponse.UserListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  
  Optional<Profile> findByMemberId(Long memberId);
  
  @Query("select p from Profile p where p.member.name like :keyword or p.member.groupName like :keyword or p.member.jibunAddress like :keyword or p.member.roadAddress like :keyword or p.member.detailAddress like :keyword or p.member.extraAddress like :keyword")
  Page<UserListResponse> findByUserLikeKeyword(@Param(value = "keyword")String keyword, Pageable pageable);
}
