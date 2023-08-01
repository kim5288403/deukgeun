package com.example.deukgeun.trainer.application.dto.response;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class TrainerResponse {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        private String email;

        private String name;

        private Group group;

        private Gender gender;

        private Address address;

        private String introduction;

        private Integer price;

        public Info(Trainer trainer) {
            this.email = trainer.getEmail();
            this.name = trainer.getName();
            this.group = trainer.getGroup();
            this.address = trainer.getAddress();
            this.gender = trainer.getGender();
            this.price = trainer.getPrice();
            this.introduction = trainer.getIntroduction();
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private String email;

        private String name;

        private Gender gender;

        private String introduction;

        private Integer price;

        private Group group;

        private Address address;

        private Profile profile;

        private Post post;

        private List<License> licenses;

        public Detail(Trainer trainer) {
            this.email = trainer.getEmail();
            this.name = trainer.getName();
            this.price = trainer.getPrice();
            this.introduction = trainer.getIntroduction();
            this.gender = trainer.getGender();
            this.group = trainer.getGroup();
            this.address = trainer.getAddress();
            this.profile = trainer.getProfile();
            this.licenses = trainer.getLicenses();
        }

    }



}
