package com.example.deukgeun.trainer.response;

import com.example.deukgeun.global.entity.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;

@Data
@NoArgsConstructor
public class PostResponse {
  private Long trainerId;

  private Long postId;

  private String html;

  public PostResponse(Post post) {
      this.postId = post.getId();
      this.trainerId = post.getTrainerId();
      this.html = HtmlUtils.htmlUnescape(post.getHtml());
  }
}
