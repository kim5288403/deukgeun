package com.example.deukgeun.commom.response;

import com.example.deukgeun.commom.entity.JobPosting;
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
    public static class ListResponse {
        private Long id;

        private String title;

        private String startDate;

        private String endDate;

        private String address;

        public ListResponse(JobPosting jobPosting) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.id = jobPosting.getId();
            this.title = jobPosting.getTitle();
            this.startDate = jobPosting.getStartDate().format(formatter);
            this.endDate = jobPosting.getEndDate().format(formatter);
            this.address = jobPosting.getPostcode()
                            + " " + jobPosting.getDetailAddress()
                            + " " + jobPosting.getRoadAddress()
                            + " " + jobPosting.getJibunAddress()
                            + " " + jobPosting.getExtraAddress();
        }
    }
}
