package com.example.deukgeun.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class MultipartFileUtil {

    public static String getUUIDPath(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("Empty fileName");
        }

        String uuid = UUID.randomUUID().toString();

        return uuid + "_" + fileName;
    }

    public static boolean isSupportedContentType(MultipartFile file) {
        String fileType = Objects.requireNonNull(file.getContentType());
        return fileType.equals("image/png") || fileType.equals("image/jpg") || fileType.equals("image/jpeg");
    }

    public static void validMimeType(String mimeType) throws Exception {
        String[] allowedMimeTypes = new String[]{
                "image/gif",
                "image/jpeg",
                "image/pjpeg",
                "image/x-png",
                "image/png",
                "image/svg+xml"
        };

        if (!Arrays.asList(allowedMimeTypes).contains(mimeType.toLowerCase())) {
            throw new Exception("Image does not meet the validation.");
        }
    }

    public static void validContentType(String contentType) throws Exception {
        if (contentType == null ||
                !contentType.toLowerCase().contains("multipart/form-data")) {

            throw new Exception("Invalid contentType. It must be multipart/form-data");
        }
    }
}
