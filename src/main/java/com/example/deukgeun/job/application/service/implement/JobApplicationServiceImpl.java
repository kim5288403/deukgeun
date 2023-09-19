package com.example.deukgeun.job.application.service.implement;

import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.application.dto.response.JobResponse;
import com.example.deukgeun.job.application.service.JobApplicationService;
import com.example.deukgeun.job.domain.dto.SaveJobDTO;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.service.JobDomainService;
import com.example.deukgeun.job.infrastructure.persistence.mapper.JobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobDomainService jobDomainService;
    private final JobMapper jobMapper;

    @Override
    public JobResponse.Detail getDetail(Long id) {
        return jobMapper.toJobResponseDetail(findById(id));
    }

    /**
     * 공고의 식별자와 회원의 식별자를 사용하여 공고가 존재하는지 확인합니다.
     *
     * @param id       공고의 식별자입니다.
     * @param memberId 회원의 식별자입니다.
     * @return 공고의 존재하는 경우 true, 그렇지 않으면 false를 반환합니다.
     */
    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobDomainService.existsByIdAndMemberId(id, memberId);
    }

    /**
     * 공고의 식별자를 사용하여 해당 공고를 조회합니다.
     *
     * @param id 공고의 식별자입니다.
     * @return 조회된 공고 객체입니다.
     */
    @Override
    public Job findById(Long id) {
        return jobDomainService.findById(id);
    }

    /**
     * 키워드를 기반으로 공고 목록을 페이징하여 조회합니다.
     *
     * @param keyword      검색 키워드입니다.
     * @param currentPage  현재 페이지 번호입니다.
     * @return 키워드를 기반으로 페이징된 공고 목록을 담은 페이지 객체입니다.
     */
    @Override
    public Page<JobResponse.List> getListByKeyword(String keyword, int currentPage) {
        // 현재 페이지 번호와 페이지 크기를 사용하여 PageRequest를 생성합니다.
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        // 검색 키워드에 와일드카드를 추가합니다.
        String likeKeyword = "%" + keyword + "%";
        // 공고 도메인 서비스를 사용하여 키워드를 기반으로 공고 목록을 페이징하여 조회합니다.
        Page<Job> job = jobDomainService.findListByKeyword(likeKeyword, pageRequest);
        System.out.println("================================");
        System.out.println(job.getContent().get(0).getJobAddress().getDetailAddress());
        System.out.println("================================");
        return job.map(jobMapper::toJobResponseList);
    }

    /**
     * 회원의 식별자를 기반으로 해당 회원의 공고 목록을 페이징하여 조회합니다.
     *
     * @param memberId     회원의 식별자입니다.
     * @param currentPage  현재 페이지 번호입니다.
     * @return 회원의 공고 목록을 페이징한 페이지 객체입니다.
     */
    @Override
    public Page<JobResponse.List> getListByMemberId(Long memberId, int currentPage) {
        // 현재 페이지 번호와 페이지 크기를 사용하여 PageRequest를 생성합니다.
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        // 공고 도메인 서비스를 사용하여 회원의 식별자를 기반으로 공고 목록을 페이징하여 조회합니다.
        Page<Job> job = jobDomainService.findListByMemberId(memberId, pageRequest);

        return job.map(jobMapper::toJobResponseList);
    }

    /**
     * 공고를 등록합니다.
     *
     * @param saveJobRequest 공고 등록 요청 DTO입니다.
     * @param memberId        공고를 등록할 회원의 식별자입니다.
     * @return 등록된 공고 객체입니다.
     */
    @Override
    public Job save(SaveJobRequest saveJobRequest, Long memberId) {
        SaveJobDTO saveJobDTO = jobMapper.toSaveJobDto(memberId, saveJobRequest);

        return jobDomainService.save(saveJobDTO);
    }

    /**
     * 공고의 활성 상태를 업데이트합니다.
     *
     * @param isActive 공고의 활성 상태를 나타내는 정수 값입니다.
     *                 1은 활성 상태를 나타냅니다.
     *                 0은 비활성 상태를 나타냅니다.
     * @param id       공고의 식별자입니다.
     */
    @Override
    public void updateIsActiveByJobId(int isActive, Long id) {
        jobDomainService.updateIsActiveByJobId(isActive, id);
    }
}
