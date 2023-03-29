package com.example.deukgeun.trainer.service.implement;

import java.io.File;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.repository.PostRepository;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
  
  private final PostRepository postRepository;
  
  public void save(PostRequest request) {
    String content = request.getContent();
    String html = StringEscapeUtils.escapeHtml3(content);
    Post post = PostRequest.create(html);
    postRepository.save(post);
  }
  
  public void saveImage() {
    
  }
  
  public void validContentType(String contentType) throws Exception {
    if (contentType == null ||
        contentType.toLowerCase().indexOf("multipart/form-data") == -1) {

        throw new Exception("Invalid contentType. It must be multipart/form-data");
    }
  }
  
  public void validMimeType(String mimeType, File uploads, String name) throws Exception {
    String[] allowedMimeTypes = new String[] {
        "image/gif",
        "image/jpeg",
        "image/pjpeg",
        "image/x-png",
        "image/png",
        "image/svg+xml"
    };

    if (!ArrayUtils.contains(allowedMimeTypes, mimeType.toLowerCase())) {

        File file = new File(uploads + name);
        if (file.exists()) {
            file.delete();
        }

        throw new Exception("Image does not meet the validation.");
    }
  }
  
  
}
