package com.example.deukgeun.trainer.infrastructure.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.deukgeun.global.util.MultipartFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String AWS_S3_BUCKET;

    @Value("${cloud.aws.s3.src}")
    private String AWS_S3_SRC;

    /**
     * Amazon S3 버킷에서 주어진 경로에 있는 객체(파일 또는 객체)를 삭제합니다.
     *
     * @param path 삭제할 객체의 경로
     */
    public void delete(String path) {
        // 경로를 '/' 문자로 분리하고, 가장 마지막 요소(파일 또는 객체 이름)를 추출합니다.
        String deletePath = Arrays.stream(path.split("/"))
                .reduce((first, second) -> second)
                .orElse(null);

        // Amazon S3 클라이언트를 사용하여 객체가 존재하는지 확인합니다.
        if (amazonS3Client.doesObjectExist(AWS_S3_BUCKET, deletePath)) {
            // 객체가 존재하는 경우, 해당 객체를 Amazon S3 버킷에서 삭제합니다.
            amazonS3Client.deleteObject(AWS_S3_BUCKET, deletePath);
        }
    }

    /**
     * 클라이언트로부터 이미지 파일을 수신하여 Amazon S3에 업로드하고 업로드된 이미지의 링크를 반환합니다.
     *
     * @param request  HTTP 요청 객체
     * @return 이미지 업로드된 링크를 포함하는 맵 객체
     * @throws Exception 이미지 업로드 및 유효성 검사 중 발생할 수 있는 예외
     */
    public Map<Object, Object> saveImageToS3(HttpServletRequest request) throws Exception {
        // HTTP 요청에서 파일 파트를 가져옵니다.
        Part filePart = request.getPart("file");
        // 파일의 컨텐츠 타입을 확인합니다.
        String contentType = filePart.getContentType();

        // 요청의 컨텐츠 타입이 "multipart/form-data"인지 확인합니다.
        MultipartFileUtil.validContentType(request.getContentType());

        // 파일의 MIME 타입이 허용되는 이미지 타입인지 확인합니다.
        MultipartFileUtil.validMimeType(contentType);

        // 이미지 파일을 Amazon S3에 업로드하고 업로드된 이미지의 링크를 가져옵니다.
        String filePath = uploadByPart(filePart);

        // 업로드된 이미지의 링크를 responseData 맵에 저장합니다.
        Map<Object, Object> responseData = new HashMap<>();
        responseData.put("link", filePath);

        return responseData;
    }

    /**
     * 주어진 MultipartFile을 사용하여 파일을 Amazon S3 버킷에 업로드하고, 업로드된 파일의 경로를 반환합니다.
     *
     * @param file 업로드할 파일 정보를 담고 있는 MultipartFile 객체
     * @return 업로드된 파일의 경로
     * @throws IOException 파일 업로드 중 발생한 입출력 예외
     */
    public String uploadByMultiPartFile(MultipartFile file) throws IOException {
        // 업로드할 파일의 저장할 이름을 생성합니다.
        String fileName = MultipartFileUtil.getUUIDPath(file.getOriginalFilename());

        // 파일 메타데이터를 설정합니다.
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // 파일을 Amazon S3 버킷에 업로드하고 업로드된 파일의 경로를 반환합니다.
        return upload(fileName, file.getInputStream(), metadata);
    }

    /**
     * 주어진 Part를 사용하여 파일을 Amazon S3 버킷에 업로드하고, 업로드된 파일의 경로를 반환합니다.
     *
     * @param part 업로드할 파일 정보를 담고 있는 Part 객체
     * @return 업로드된 파일의 경로
     * @throws IOException 파일 업로드 중 발생한 입출력 예외
     */
    public String uploadByPart(Part part) throws IOException {
        // 업로드할 파일의 저장할 이름을 생성합니다.
        String fileName = MultipartFileUtil.getUUIDPath(part.getName());

        // 파일 메타데이터를 설정합니다.
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(part.getContentType());
        metadata.setContentLength(part.getSize());

        // 파일을 Amazon S3 버킷에 업로드하고 업로드된 파일의 경로를 반환합니다.
        return upload(fileName, part.getInputStream(), metadata);
    }

    /**
     * 주어진 파일 이름, 입력 스트림 및 메타데이터를 사용하여 파일을 Amazon S3 버킷에 업로드하고 업로드된 파일의 경로를 반환합니다.
     *
     * @param fileName 업로드된 파일의 저장할 이름
     * @param input    업로드할 파일의 입력 스트림
     * @param metadata 파일의 메타데이터 정보
     * @return 업로드된 파일의 경로
     */
    private String upload(String fileName, InputStream input, ObjectMetadata metadata) {
        // Amazon S3 클라이언트를 사용하여 파일을 Amazon S3 버킷에 업로드합니다.
        amazonS3Client.putObject(AWS_S3_BUCKET, fileName, input, metadata);

        // 업로드된 파일의 경로를 반환합니다.
        return AWS_S3_SRC + fileName;
    }
}
