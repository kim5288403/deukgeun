package com.example.deukgeun.trainer.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.response.UserListResponse;

public interface UserRepository extends JpaRepository<User, Long> {
  List<UserListResponse> findByNameOrGroupName(String name, String groupName);

  boolean existsByEmail(String email);
}
