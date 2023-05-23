package com.example.deukgeun.trainer.profile;

import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ProfileGetUUIDPathTest {

    @Autowired
    private ProfileServiceImpl profileService;

    @Mock
    private ProfileServiceImpl mockProfileService;

    @Test
    public void shouldReturnUUIDPathWithFileName() {
        // Given
        String fileName = "example.txt";
        String uuid = UUID.randomUUID().toString();
        given(mockProfileService.getUUIDPath(fileName)).willReturn(uuid + "_" + fileName);

        // When
        String result = mockProfileService.getUUIDPath(fileName);

        // Then
        String expected = uuid + "_" + fileName;
        assertEquals(expected, result);
    }


    @Test
    void shouldReturnUniqueUUIDPathForFileName() {
        // Given
        String fileName = "example.txt";

        // When
        String result1 = profileService.getUUIDPath(fileName);
        String result2 = profileService.getUUIDPath(fileName);

        // Then
        assertNotEquals(result1, result2);
    }

    @Test
    void shouldReturnUUIDPathForEmptyFileName() {
        // Given
        String fileName = "";
        String uuid = UUID.randomUUID().toString();
        given(mockProfileService.getUUIDPath(fileName)).willReturn(uuid + "_" + fileName);

        // When
        String result = mockProfileService.getUUIDPath(fileName);

        // Then
        String expected = uuid + "_";
        assertEquals(expected, result);
    }
}
