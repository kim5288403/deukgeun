package com.example.deukgeun.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.deukgeun.global.util.MultipartFileUtil;
import com.example.deukgeun.trainer.infrastructure.s3.S3Service;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class S3serviceTest {
    @InjectMocks
    private S3Service s3Service;

    @Mock
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String AWS_S3_BUCKET;

    @Value("${cloud.aws.s3.src}")
    private String AWS_S3_SRC;

    @BeforeAll
    public static void staticSetup() {
        mockStatic(MultipartFileUtil.class);
    }

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(s3Service, "AWS_S3_BUCKET", AWS_S3_BUCKET);
        ReflectionTestUtils.setField(s3Service, "AWS_S3_SRC", AWS_S3_SRC);
    }

    @Test
    public void givenValidSrc_whenDelete_thenS3DeleteCalled() {
        // Given
        String src = "http://example.com/images/image.jpg";
        given(amazonS3Client.doesObjectExist(anyString(), anyString())).willReturn(true);

        // When
        s3Service.delete(src);

        // Then
        verify(amazonS3Client, times(1)).deleteObject(anyString(), anyString());
    }
    @Test
    public void givenValidRequestAndResponse_whenSaveImageToS3_thenReturnLinkInMap() throws Exception {
        // Given
        String filePath = "filePath";
        Part filePartMock = mock(Part.class);
        HttpServletRequest requestMock = mock(HttpServletRequest.class);

        given(requestMock.getPart(anyString())).willReturn(filePartMock);
        given(filePartMock.getName()).willReturn("fileName");
        given(filePartMock.getContentType()).willReturn("image/jpeg");
        given(MultipartFileUtil.getUUIDPath(anyString())).willReturn(filePath);

        // When
        Map<Object, Object> responseData = s3Service.saveImageToS3(requestMock);

        // Then
        assertNotNull(responseData);
        assertEquals(1, responseData.size());
        assertTrue(responseData.containsKey("link"));
    }
}
