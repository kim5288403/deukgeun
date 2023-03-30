package com.example.deukgeun.trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.deukgeun.trainer.entity.PostImage;

@Transactional
public interface PostImageRepository extends JpaRepository<PostImage, Long>{
  Long deleteByPath(String path);
  
}
