package com.example.deukgeun.trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deukgeun.trainer.entity.TrainerUser;

public interface TrainerUserRepository extends JpaRepository<TrainerUser, Long>{

}
