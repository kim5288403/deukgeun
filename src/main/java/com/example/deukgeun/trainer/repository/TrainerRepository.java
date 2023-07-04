package com.example.deukgeun.trainer.repository;


import com.example.deukgeun.trainer.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
  Optional<Trainer> findByEmail(String email);
   
  boolean existsByEmail(String email);
}
