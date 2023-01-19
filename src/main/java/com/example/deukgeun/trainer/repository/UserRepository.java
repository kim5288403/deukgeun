package com.example.deukgeun.trainer.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deukgeun.trainer.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByNameOrGroupName(String name, String groupName);
}
