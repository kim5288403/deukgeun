package com.example.deukgeun.trainer.response;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.Trainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class TrainerResponse {
    private String email;

    private String name;

    private GroupStatus groupStatus;

    private Gender gender;

    private String groupName;

    private String postcode;

    private String jibunAddress;

    private String roadAddress;

    private String detailAddress;

    private String extraAddress;

    private String introduction;

    private Integer price;

    public TrainerResponse(Trainer trainer) {
        this.email = trainer.getEmail();
        this.name = trainer.getName();
        this.groupStatus = trainer.getGroupStatus();
        this.groupName = trainer.getGroupName();
        this.postcode = trainer.getPostcode();
        this.jibunAddress = trainer.getJibunAddress();
        this.roadAddress = trainer.getRoadAddress();
        this.detailAddress = trainer.getDetailAddress();
        this.extraAddress = trainer.getExtraAddress();
        this.gender = trainer.getGender();
        this.price = trainer.getPrice();
        this.introduction = trainer.getIntroduction();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrainerListResponse {

        private Long id;

        private String name;

        private String path;

        private GroupStatus groupStatus;

        private String groupName;

        private String introduction;

        public TrainerListResponse(Profile profile) {
            this.id = profile.getTrainerId();
            this.path = profile.getPath();
            this.name = profile.getTrainer().getName();
            this.groupStatus = profile.getTrainer().getGroupStatus();
            this.groupName = profile.getTrainer().getGroupName();
            this.introduction = profile.getTrainer().getIntroduction();

        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrainerListPaginationResponse {

        private List<TrainerListResponse> list;

        private Integer totalPages;

        private Integer currentPage;

        public TrainerListPaginationResponse(Page<TrainerListResponse> page, Integer currentPage) {
            this.list = page.getContent();
            this.totalPages = page.getTotalPages();
            this.currentPage = currentPage;
        }
    }

}
