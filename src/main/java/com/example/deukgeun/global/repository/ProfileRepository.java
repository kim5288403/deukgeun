package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  Optional<Profile> findByTrainerId(Long trainerId);
  
}
