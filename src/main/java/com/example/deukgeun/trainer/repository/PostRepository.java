package com.example.deukgeun.trainer.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.example.deukgeun.trainer.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
  Optional<Post> findByUserId(Long user_id);
  
  @Modifying
  @Transactional
  @Query(value = "update trainer_post m set m.html = :html where m.user_id = :user_id", nativeQuery = true)
  int update(@Param(value = "user_id")Long user_id, @Param(value = "html")String html);
}
