package com.example.deukgeun.member.service;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.global.repository.ApplicantRepository;
import com.example.deukgeun.member.response.ApplicantResponse;
import com.example.deukgeun.member.service.implement.ApplicantServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ApplicantServiceTest {
    @InjectMocks
    private ApplicantServiceImpl applicantService;

    @Mock
    private ApplicantRepository applicantRepository;

    @Test
    void givenExistingJobPostingId_whenGetByJobPostingId_thenReturnsMatching() {
        // Given
        Long jobPostingId = 123L;
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);

        ApplicantResponse.ListResponse list1 = new ApplicantResponse.ListResponse();
        list1.setJobPostingId(jobPostingId);
        ApplicantResponse.ListResponse list2 = new ApplicantResponse.ListResponse();
        list2.setJobPostingId(jobPostingId);

        List<ApplicantResponse.ListResponse> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        Page<ApplicantResponse.ListResponse> page = new PageImpl<>(list, pageable, list.size());

        given(applicantRepository.findByJobPostingId(jobPostingId, pageable)).willReturn(page);

        // When
        Page<ApplicantResponse.ListResponse> result = applicantService.getByJobPostingId(jobPostingId, currentPage);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(applicantRepository, times(1)).findByJobPostingId(jobPostingId, pageable);
    }

    @Test
    public void givenApplicantExists_whenUpdateIsSelectedByApplicantId_thenIsSelectedUpdated() {
        // Given
        Long applicantId = 1L;
        Applicant applicant = Applicant
                .builder()
                .id(applicantId)
                .build();

        given(applicantRepository.findById(anyLong())).willReturn(Optional.of(applicant));

        // When
        applicantService.updateIsSelectedByApplicantId(applicantId, 1);

        // Then
        verify(applicantRepository, times(1)).findById(applicantId);
        verify(applicantRepository, times(1)).save(applicant);
    }
}
