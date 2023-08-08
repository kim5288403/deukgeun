package com.example.deukgeun.job.application.dto.response;

import com.example.deukgeun.job.domain.model.aggregate.JobPosting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class JobPostingResponse {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class List {
        private Long id;

        private Long memberId;

        private String title;

        private String startDate;

        private String endDate;

        private String address;

        public List(JobPosting jobPosting) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.id = jobPosting.getId();
            this.memberId = jobPosting.getMemberId();
            this.title = jobPosting.getTitle();
            this.startDate = jobPosting.getStartDate().format(formatter);
            this.endDate = jobPosting.getEndDate().format(formatter);
            this.address = jobPosting.getAddress().getPostcode()
                            + " " + jobPosting.getAddress().getDetailAddress()
                            + " " + jobPosting.getAddress().getRoadAddress()
                            + " " + jobPosting.getAddress().getJibunAddress()
                            + " " + jobPosting.getAddress().getExtraAddress();
        }

    }
}
