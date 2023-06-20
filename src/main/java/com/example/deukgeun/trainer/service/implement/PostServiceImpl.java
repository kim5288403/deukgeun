package com.example.deukgeun.trainer.service.implement;

import com.example.deukgeun.trainer.entity.Member;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.repository.PostRepository;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.service.PostService;
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
public class PostServiceImpl implements PostService {

    private final MemberServiceImpl memberService;
    private final PostRepository postRepository;

    @Value("${trainer.post.filePath}")
    private String postFilePath;

    @Value("${trainer.post.url}")
    private String postUrl;

    /**
     * 게시글을 업로드합니다.
     *
     * @param request   PostRequest 객체
     * @param authToken 인증 토큰
     * @throws Exception 예외 발생 시
     */
    public void upload(PostRequest request, String authToken) throws Exception {
        // 인증 토큰을 이용하여 사용자 정보를 가져옵니다.
        Member member = memberService.getByAuthToken(authToken);
        Long memberId = member.getId();

        // 해당 사용자의 게시글을 조회합니다.
        Post post = postRepository.findByUserId(memberId).orElse(null);

        // 게시글 내용을 HTML 이스케이프하여 저장합니다.
        String content = request.getContent();
        String html = HtmlUtils.htmlEscape(content);

        if (post != null) {
            // 이미 게시글이 존재하는 경우, 게시글을 업데이트합니다.
            update(memberId, html);
        } else {
            // 게시글이 존재하지 않는 경우, 새로운 게시글을 저장합니다.
            save(memberId, html);
        }
    }

    /**
     * 새로운 게시글을 저장합니다.
     *
     * @param userId 사용자 ID
     * @param html   HTML 이스케이프된 게시글 내용
     */
    public void save(Long userId, String html) {
        Post post = PostRequest.create(html, userId);
        postRepository.save(post);
    }

    /**
     * 기존 게시글을 업데이트합니다.
     *
     * @param userId 사용자 ID
     * @param html   HTML 이스케이프된 게시글 내용
     */
    public void update(Long userId, String html) {
        postRepository.update(userId, html);
    }

    /**
     * 이미지를 서버에 저장하고 이미지 링크를 반환합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return 이미지 링크를 담은 맵
     * @throws Exception 예외가 발생한 경우
     */
    public Map<Object, Object> saveImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 이미지를 저장할 디렉토리 생성
        File uploads = new File(postFilePath);
        Part filePart = request.getPart("file");
        String contentType = filePart.getContentType();

        // 요청의 컨텐츠 타입 검증
        validContentType(request.getContentType());

        // 파일 확장자 추출
        String extension = getExtensionFromContentType(contentType);
        String name = UUID.randomUUID().toString() + extension;
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

    /**
     * 컨텐츠 유형(content type)에서 확장자(extension)를 추출합니다.
     *
     * @param contentType 컨텐츠 유형 문자열
     * @return 추출된 확장자 문자열 (예: ".jpg", ".png")
     */
    private String getExtensionFromContentType(String contentType) {
        if (contentType != null) {
            int slashIndex = contentType.lastIndexOf("/");
            if (slashIndex != -1 && slashIndex < contentType.length() - 1) {
                return "." + contentType.substring(slashIndex + 1);
            }
        }
        return "";
    }

    /**
     * 서버에 이미지를 저장합니다.
     *
     * @param filePart 이미지 파일의 Part 객체
     * @param writer   PrintWriter 객체
     * @param file     저장할 파일 객체
     */
    public void saveServerImage(Part filePart, PrintWriter writer, File file) {
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath());
        } catch (Exception e) {
            writer.println("<br/> ERROR: " + e);
        }
    }

    /**
     * contentType 이 유효한지 확인합니다.
     *
     * @param contentType 확인할 contentType 문자열
     * @throws Exception contentType 이 유효하지 않을 경우 예외를 던집니다.
     */
    public void validContentType(String contentType) throws Exception {
        if (contentType == null ||
                !contentType.toLowerCase().contains("multipart/form-data")) {

            throw new Exception("Invalid contentType. It must be multipart/form-data");
        }
    }

    /**
     * mimeType 이 유효한지 확인합니다.
     *
     * @param mimeType 확인할 mimeType 문자열
     * @param uploads  이미지가 저장될 디렉토리
     * @param name     이미지 파일 이름
     * @throws Exception mimeType 이 유효하지 않을 경우 예외를 던집니다.
     */
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
            deleteFileToDirectory(uploads + "/" + name);

            throw new Exception("Image does not meet the validation.");
        }
    }

    /**
     * 요청된 URI 에 해당하는 서버 이미지 파일을 가져옵니다.
     *
     * @param getRequestURI 요청된 URI
     * @return 요청된 URI 에 해당하는 서버 이미지 파일
     */
    public File getServerImage(String getRequestURI) {
        String[] url = getRequestURI.split("/");
        String filename = url[url.length - 1];

        return new File(postFilePath, filename);
    }

    /**
     * 주어진 URL에서 파일 이름을 추출하여 파일 경로를 반환합니다.
     *
     * @param src URL 문자열
     * @return 파일 경로
     */
    public String getFilePathFromUrl(String src) {
        String[] url = src.split("/");
        String filename = url[url.length - 1];
        return postFilePath + "\\" + filename;
    }

    /**
     * 서버에 저장된 이미지를 삭제합니다.
     *
     * @param path 이미지 파일 경로
     */
    public void deleteFileToDirectory(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 사용자 ID에 해당하는 게시글을 조회합니다.
     *
     * @param id 사용자 ID
     * @return 조회된 게시글
     * @throws EntityNotFoundException 게시글을 찾을 수 없을 때 발생하는 예외
     */
    public Post findByUserId(Long id) throws EntityNotFoundException {
        return postRepository.findByUserId(id).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    /**
     * 게시글을 삭제합니다.
     *
     * @param id 삭제할 게시글의 ID
     */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
