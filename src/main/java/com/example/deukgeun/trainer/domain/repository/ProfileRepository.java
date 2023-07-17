package com.example.deukgeun.trainer.domain.repository;

import com.example.deukgeun.trainer.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  Optional<Profile> findByTrainerId(Long trainerId);
  
}
