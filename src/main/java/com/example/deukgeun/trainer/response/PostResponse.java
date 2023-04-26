package com.example.deukgeun.trainer.response;

import com.example.deukgeun.trainer.entity.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;

@Data
@NoArgsConstructor
public class PostResponse {
  private Long user_id;
  
  private Long post_id;
  
  private String html;
  
  public PostResponse(Post post) {
      this.post_id = post.getId();
      this.user_id = post.getUserId();
      this.html = HtmlUtils.htmlUnescape(post.getHtml());
  }
}
