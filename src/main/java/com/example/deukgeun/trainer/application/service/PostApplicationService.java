package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.domain.model.entity.Post;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

public interface PostApplicationService {
    void deleteById(Long id);
    void deleteFileToDirectory(File file);
    Post findByTrainerId(Long id) throws EntityNotFoundException;
    File getServerImage(String getRequestURI);
    String getFilePathFromUrl(String src);
    void save(Long userId, String html);
    Map<Object, Object> saveImage(HttpServletRequest request, HttpServletResponse response) throws Exception;
    void saveServerImage(Part filePart, PrintWriter writer, File file);
    void upload(PostRequest request, Long trainerId) throws Exception;
    void updateHtml(Long trainerId, String html);
    void validMimeType(String mimeType, File uploads, String name) throws Exception;
    void validContentType(String contentType) throws Exception;
}
