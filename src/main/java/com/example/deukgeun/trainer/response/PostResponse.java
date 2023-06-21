package com.example.deukgeun.trainer.response;

import com.example.deukgeun.trainer.entity.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;

@Data
@NoArgsConstructor
public class PostResponse {
  private Long memberId;

  private Long postId;

  private String html;

  public PostResponse(Post post) {
      this.postId = post.getId();
      this.memberId = post.getMemberId();
      this.html = HtmlUtils.htmlUnescape(post.getHtml());
  }
}
