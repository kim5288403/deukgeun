package com.example.deukgeun.trainer.profile;

import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class GetUUIDPathTest {

    @Autowired
    private ProfileServiceImpl profileService;

    @Mock
    private ProfileServiceImpl mockProfileService;

    @Test
    public void shouldReturnUUIDPathForValidFileName() throws IOException {
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
    void shouldReturnUniqueUUIDPathForValidFileName() throws IOException {
        // Given
        String fileName = "example.txt";

        // When
        String result1 = profileService.getUUIDPath(fileName);
        String result2 = profileService.getUUIDPath(fileName);

        // Then
        assertNotEquals(result1, result2);
    }

    @Test
    void shouldThrowIOExceptionForInvalidFileName() throws IOException {
        // Given
        String fileName1 = "";
        String fileName2 = null;

        // When, Then
        assertThrows(IOException.class, () -> {
            profileService.getUUIDPath(fileName1);
        });

        assertThrows(IOException.class, () -> {
            profileService.getUUIDPath(fileName2);
        });
    }

    @Test
    void shouldNotThrowExceptionForValidFileName() {
        // Given
        String fileName = "fileName";

        // When, Then
        assertDoesNotThrow( () -> {
            profileService.getUUIDPath(fileName);
        });
    }
}
