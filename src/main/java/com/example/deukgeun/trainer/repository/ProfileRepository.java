package com.example.deukgeun.trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deukgeun.trainer.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
