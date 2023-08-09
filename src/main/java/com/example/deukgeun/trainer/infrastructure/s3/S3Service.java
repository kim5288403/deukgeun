package com.example.deukgeun.trainer.infrastructure.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.deukgeun.global.util.MultipartFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.src}")
    private String src;

    public void delete(String path) {
        String deletePath = Arrays.stream(path.split("/"))
                .reduce((first, second) -> second)
                .orElse(null);

        if (amazonS3Client.doesObjectExist(bucket, deletePath)) {
            amazonS3Client.deleteObject(bucket, deletePath);
        }
    }

    public String upload(MultipartFile file) throws IOException {
        String fileName = MultipartFileUtil.getUUIDPath(file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        return src + fileName;
    }

    public String upload(Part part) throws IOException {
        String fileName = MultipartFileUtil.getUUIDPath(part.getName());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(part.getContentType());
        metadata.setContentLength(part.getSize());

        amazonS3Client.putObject(bucket, fileName, part.getInputStream(), metadata);

        return  src + fileName;
    }

}
