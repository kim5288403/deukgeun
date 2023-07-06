package com.example.deukgeun.trainer.response;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Trainer;
import lombok.AllArgsConstructor;
import lombok.Data;

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

}
