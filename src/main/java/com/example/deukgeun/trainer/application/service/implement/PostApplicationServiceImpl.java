package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.service.PostApplicationService;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.service.PostDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostApplicationServiceImpl implements PostApplicationService {

    private final PostDomainService postDomainService;

    @Value("${trainer.post.filePath}")
    private String postFilePath;

    @Value("${trainer.post.url}")
    private String postUrl;

    public void deleteById(Long id) {
        postDomainService.deleteById(id);
    }

    public void deleteFileToDirectory(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    public Post findByTrainerId(Long id) throws EntityNotFoundException {
        return postDomainService.findByTrainerId(id);
    }

    public File getServerImage(String getRequestURI) {
        String[] url = getRequestURI.split("/");
        String filename = url[url.length - 1];

        return new File(postFilePath, filename);
    }

    public String getFilePathFromUrl(String src) {
        String[] url = src.split("/");
        String filename = url[url.length - 1];
        return postFilePath + "\\" + filename;
    }

    private String getExtensionFromContentType(String contentType) {
        if (contentType != null) {
            int slashIndex = contentType.lastIndexOf("/");
            if (slashIndex != -1 && slashIndex < contentType.length() - 1) {
                return "." + contentType.substring(slashIndex + 1);
            }
        }
        return "";
    }

    public void save(Long userId, String html) {
        postDomainService.save(html, userId);
    }

    public Map<Object, Object> saveImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 이미지를 저장할 디렉토리 생성
        File uploads = new File(postFilePath);
        Part filePart = request.getPart("file");
        String contentType = filePart.getContentType();

        // 요청의 컨텐츠 타입 검증
        validContentType(request.getContentType());

        // 파일 확장자 추출
        String extension = getExtensionFromContentType(contentType);
        String name = UUID.randomUUID() + extension;
        String linkName = postUrl + name;

        // MIME 타입 검증
        validMimeType(contentType, uploads, name);

        File file = new File(uploads, name);
        saveServerImage(filePart, response.getWriter(), file);

        // 이미지 링크를 담은 맵 생성하여 반환
        Map<Object, Object> responseData = new HashMap<>();
        responseData.put("link", linkName);

        return responseData;
    }

    public void saveServerImage(Part filePart, PrintWriter writer, File file) {
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath());
        } catch (Exception e) {
            writer.println("<br/> ERROR: " + e);
        }
    }

    public void upload(PostRequest request, Long trainerId) throws Exception {
        // 해당 사용자의 게시글을 조회합니다.
        boolean existsPost = postDomainService.existsByTrainerId(trainerId);

        // 게시글 내용을 HTML 이스케이프하여 저장합니다.
        String content = request.getContent();
        String html = HtmlUtils.htmlEscape(content);

        if (existsPost) {
            // 이미 게시글이 존재하는 경우, 게시글을 업데이트합니다.
            updateHtml(trainerId, html);
        } else {
            // 게시글이 존재하지 않는 경우, 새로운 게시글을 저장합니다.
            save(trainerId, html);
        }
    }

    public void updateHtml(Long trainerId, String html) {
        Post post = postDomainService.findByTrainerId(trainerId);
        post.updateHtml(html);
        postDomainService.update(post);
    }

    public void validContentType(String contentType) throws Exception {
        if (contentType == null ||
                !contentType.toLowerCase().contains("multipart/form-data")) {

            throw new Exception("Invalid contentType. It must be multipart/form-data");
        }
    }

    public void validMimeType(String mimeType, File uploads, String name) throws Exception {
        String[] allowedMimeTypes = new String[]{
                "image/gif",
                "image/jpeg",
                "image/pjpeg",
                "image/x-png",
                "image/png",
                "image/svg+xml"
        };

        boolean contains = Arrays.asList(allowedMimeTypes).contains(mimeType.toLowerCase());

        if (!contains) {
            File deleteFile = new File(uploads + "/" + name);
            deleteFileToDirectory(deleteFile);

            throw new Exception("Image does not meet the validation.");
        }
    }
}
