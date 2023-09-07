package com.example.deukgeun.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class MultipartFileUtil {

    /**
     * 파일 이름을 기반으로 UUID 경로를 생성합니다.
     *
     * @param fileName 파일 이름입니다.
     * @return 생성된 UUID 경로 문자열입니다.
     * @throws IOException 파일 이름이 null 또는 빈 문자열인 경우 예외를 발생시킵니다.
     */
    public static String getUUIDPath(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("Empty fileName");
        }

        String uuid = UUID.randomUUID().toString();

        return uuid + "_" + fileName;
    }

    /**
     * 업로드된 파일의 컨텐츠 유형이 지원되는 이미지 파일 유형인지 확인합니다.
     *
     * @param file 업로드된 파일입니다.
     * @return 이미지 파일 컨텐츠 유형이면 true를 반환하고, 그렇지 않으면 false를 반환합니다.
     * @throws NullPointerException 파일의 컨텐츠 유형이 null인 경우 예외를 발생시킵니다.
     */
    public static boolean isSupportedContentType(MultipartFile file) {
        String fileType = Objects.requireNonNull(file.getContentType());
        return fileType.equals("image/png") || fileType.equals("image/jpg") || fileType.equals("image/jpeg");
    }

    /**
     * 주어진 MIME 유형이 허용된 이미지 파일 유형 중 하나인지 확인합니다.
     *
     * @param mimeType 확인할 MIME 유형입니다.
     * @throws Exception 주어진 MIME 유형이 허용되지 않는 경우 예외를 발생시킵니다.
     */
    public static void validMimeType(String mimeType) throws Exception {
        String[] allowedMimeTypes = new String[]
                {
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

    /**
     * 주어진 컨텐츠 유형이 'multipart/form-data'인지 확인합니다.
     *
     * @param contentType 확인할 컨텐츠 유형 문자열입니다.
     * @throws Exception 주어진 컨텐츠 유형이 'multipart/form-data'가 아닌 경우 예외를 발생시킵니다.
     */
    public static void validContentType(String contentType) throws Exception {
        if (contentType == null || !contentType.toLowerCase().contains("multipart/form-data")) {

            throw new Exception("Invalid contentType. It must be multipart/form-data");
        }
    }
}
