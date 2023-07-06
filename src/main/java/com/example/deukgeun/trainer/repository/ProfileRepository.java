package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.trainer.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  Optional<Profile> findByTrainerId(Long trainerId);
  
}
