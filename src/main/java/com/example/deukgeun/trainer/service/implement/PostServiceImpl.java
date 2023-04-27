package com.example.deukgeun.trainer.service.implement;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.deukgeun.global.provider.JwtProvider;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.PostRepository;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.response.PostResponse;
import com.example.deukgeun.trainer.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.HtmlUtils;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
  
  private final JwtProvider jwtProvider;
  private final UserServiceImpl userService;
  private final PostRepository postRepository;
  
  @Value("${trainer.post.filePath}")
  private String postFilePath;
  
  @Value("${trainer.post.url}")
  private String postUrl;
  
  public void upload(PostRequest request, String authToken) throws Exception {
    User user = userService.getUserByAuthToken(authToken);
    Long userId = user.getId();
    
    Optional<Post> post = postRepository.findByUserId(userId);
    
    String content = request.getContent();
    String html = HtmlUtils.htmlEscape(content);

    if (post.isPresent()) {
      update(userId, html);
    } else {
      save(userId, html);
    }
  }
  
  public void save(Long user_id, String html) {
    Post post = PostRequest.create(html, user_id);
    
    postRepository.save(post);
  }
  
  public void update(Long user_id, String html) {
    
    postRepository.update(user_id, html);
  }
  
  public Map< Object, Object > saveImage(HttpServletRequest request,  HttpServletResponse response) throws Exception {
    File uploads = new File(postFilePath);
    String linkName = null;
    String name = null;
    Part filePart = request.getPart("file");

    validContentType(request.getContentType());
    
    String type = filePart.getContentType();
    String converter_type = type.substring(type.lastIndexOf("/") + 1);

    String extension = converter_type;
    extension = (extension != null && extension != "") ? "." + extension : extension;
    name = UUID.randomUUID().toString() + extension ;
    linkName = postUrl + name;
    
    validMimeType(type, uploads, name);
    
    File file = new File(uploads, name);
    saveServerImage(filePart, response.getWriter(), file);
    
    Map< Object, Object > responseData = new HashMap< Object, Object > ();
    responseData.put("link", linkName);
    
    return responseData;
  }
  
  //서버 저장
  public void saveServerImage(Part filePart, PrintWriter writer, File file) {
    try (InputStream input = filePart.getInputStream()) {
      Files.copy(input, file.toPath());
    } catch (Exception e) {
      writer.println("<br/> ERROR: " + e);
    }
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

    boolean contains = Arrays.asList(allowedMimeTypes).contains(mimeType.toLowerCase());

    if (!contains) {
      deleteServerImage(uploads + "/" + name);

      throw new Exception("Image does not meet the validation.");
    }
  }
  
  public File getServerImage(String getRequestURI) {
    String[] url = getRequestURI.split("/");
    String filename = url[url.length - 1];
    
    return new File(postFilePath, filename);
  }
  
  public void deletePostImage(String src) {
    String[] url = src.split("/");
    String filename = url[url.length - 1];
    deleteServerImage(postFilePath + filename);
  }
  
  //서버 이미지 삭제
  public void deleteServerImage(String path) {
    File file = new File(path);
    if (file.exists()) {
      file.delete();
    }
  }
  
  public Post findByUserId(Long id) throws Exception {
    return postRepository.findByUserId(id).orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다."));
  }
  
  public PostResponse getPost(String authToken) throws Exception {
    String email = jwtProvider.getUserPk(authToken);
    User user = userService.getUserByAuthToken(email);
    Post post = findByUserId(user.getId());
    
    return new PostResponse(post);
  }
  
  
}
