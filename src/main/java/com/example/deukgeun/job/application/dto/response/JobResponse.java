package com.example.deukgeun.job.application.dto.response;

import com.example.deukgeun.job.domain.model.aggregate.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class JobResponse {
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

        public List(Job job) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.id = job.getId();
            this.memberId = job.getMemberId();
            this.title = job.getTitle();
            this.startDate = job.getStartDate().format(formatter);
            this.endDate = job.getEndDate().format(formatter);
            this.address = job.getAddress().getPostcode()
                            + " " + job.getAddress().getDetailAddress()
                            + " " + job.getAddress().getRoadAddress()
                            + " " + job.getAddress().getJibunAddress()
                            + " " + job.getAddress().getExtraAddress();
        }

    }
}
