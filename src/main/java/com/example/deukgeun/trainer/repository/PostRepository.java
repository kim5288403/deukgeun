package com.example.deukgeun.trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.deukgeun.trainer.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

}
