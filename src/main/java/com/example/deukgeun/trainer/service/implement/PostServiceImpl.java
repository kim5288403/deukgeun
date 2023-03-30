package com.example.deukgeun.trainer.service.implement;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.deukgeun.global.provider.JwtProvider;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.entity.PostImage;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.PostImageRepository;
import com.example.deukgeun.trainer.repository.PostRepository;
import com.example.deukgeun.trainer.request.PostImageRequest;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
  
  private final JwtProvider jwtProvider;
  private final UserServiceImpl userService;
  private final PostRepository postRepository;
  private final PostImageRepository postImageRepository;
  
  @Value("${trainer.post.filePath}")
  private String postFilePath;
  
  @Value("${trainer.post.url}")
  private String postUrl;
  
  public void save(PostRequest request) {
    String content = request.getContent();
    String html = StringEscapeUtils.escapeHtml3(content);
    Post post = PostRequest.create(html);
    postRepository.save(post);
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
    serverSaveImage(filePart, response.getWriter(), file);
    DBSaveImage(request.getHeader("Authorization").replace("Bearer ", ""), linkName);
    
    Map< Object, Object > responseData = new HashMap< Object, Object > ();
    responseData.put("link", linkName);
    
    return responseData;
  }
  
  //DB 게시물 이미지 저장
  public void DBSaveImage(String authToken, String path) throws Exception {
    String email = jwtProvider.getUserPk(authToken);
    User user = userService.getUser(email);
    PostImage postImage = PostImageRequest.create(user.getId(), path);
    postImageRepository.save(postImage);
  }
  
  //서버 저장
  public void serverSaveImage(Part filePart, PrintWriter writer, File file) {
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

    if (!ArrayUtils.contains(allowedMimeTypes, mimeType.toLowerCase())) {
      deleteServerImage(uploads + "/" + name);

        throw new Exception("Image does not meet the validation.");
    }
  }
  
  public File getServerImage(String getRequestURI) {
    String[] url = getRequestURI.split("/");
    String filename = url[url.length-1];
    
    return new File(postFilePath, filename);
  }
  
  public void postImageDelete(String src) throws Exception {
    String[] url = src.split("/");
    String filename = url[url.length-1];
    deleteServerImage(postFilePath + filename);
    deleteDBImage(src);
  }
  
  //서버 이미지 삭제
  public void deleteServerImage(String path) {
    File file = new File(path);
    if (file.exists()) {
      file.delete();
    }
  }
  
  //DB 이미지 삭제
  public void deleteDBImage(String path) {
    postImageRepository.deleteByPath(path);
  }
  
  
}
