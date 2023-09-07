package com.example.deukgeun.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {
    @Value("${cloud.aws.credentials.accessKey}")
    private String ACCESS_KEY;
    @Value("${cloud.aws.credentials.secretKey}")
    private String SECRET_KEY;
    @Value("${cloud.aws.region.static}")
    private String REGION;

    /**
     * 이 메서드는 Amazon S3 클라이언트 Bean을 생성하고 구성합니다.
     *
     * @return AmazonS3Client 객체를 반환합니다.
     */
    @Bean
    public AmazonS3Client amazonS3Client() {
        // AWS 액세스 키와 시크릿 키를 사용하여 AWS 자격 증명을 설정합니다.
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        // Amazon S3 클라이언트 빌더를 사용하여 클라이언트를 생성 및 구성합니다.
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(REGION)// 클라이언트의 리전(region)을 설정합니다.
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
