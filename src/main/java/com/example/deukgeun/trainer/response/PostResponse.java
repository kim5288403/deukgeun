package com.example.deukgeun.trainer.response;

import org.apache.commons.text.StringEscapeUtils;
import com.example.deukgeun.trainer.entity.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostResponse {
  private Long user_id;
  
  private Long post_id;
  
  private String html;
  
  public PostResponse(Post post) {
      this.post_id = post.getId();
      this.user_id = post.getUserId();
      this.html = StringEscapeUtils.unescapeHtml3(post.getHtml());
  }
}
