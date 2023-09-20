package com.example.deukgeun.trainer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponse {
  private Long postId;

  private String html;
}
