package com.example.deukgeun.trainer.domain.model.entity;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import lombok.Getter;

@Getter
public class Trainer {

  private Long id;

  private String name;

  private String email;

  private String password;

  private GroupStatus groupStatus;

  private Gender gender;

  private String groupName;

  private String postcode;

  private String jibunAddress;

  private String roadAddress;

  private String detailAddress;

  private String extraAddress;

  private Integer price;

  private String introduction;

  public Trainer(
          Long id,
          String name,
          String email,
          String password,
          GroupStatus groupStatus,
          String groupName,
          String postcode,
          String jibunAddress,
          String roadAddress,
          String detailAddress,
          String extraAddress,
          Gender gender,
          Integer price,
          String introduction
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.groupStatus = groupStatus;
    this.groupName = groupName;
    this.postcode = postcode;
    this.jibunAddress = jibunAddress;
    this.roadAddress = roadAddress;
    this.detailAddress = detailAddress;
    this.extraAddress = extraAddress;
    this.gender = gender;
    this.price = price;
    this.introduction = introduction;
  }

  public static Trainer create(
          String name,
          String email,
          String password,
          GroupStatus groupStatus,
          String groupName,
          String postcode,
          String jibunAddress,
          String roadAddress,
          String detailAddress,
          String extraAddress,
          Gender gender,
          Integer price,
          String introduction
  ) {
    Long id = LongIdGeneratorUtil.gen();
    return new Trainer(
            id,
            name,
            email,
            password,
            groupStatus,
            groupName,
            postcode,
            jibunAddress,
            roadAddress,
            detailAddress,
            extraAddress,
            gender,
            price,
            introduction
            );
  };

  public void updateInfo(UpdateInfoRequest request) {
    this.email = request.getEmail();
    this.name = request.getName();
    this.gender = request.getGender();
    this.postcode = request.getPostcode();
    this.jibunAddress = request.getJibunAddress();
    this.roadAddress = request.getRoadAddress();
    this.detailAddress = request.getDetailAddress();
    this.extraAddress = request.getExtraAddress();
    this.price = request.getPrice();
    this.groupStatus = request.getGroupStatus();
    this.groupName = request.getGroupName();
    this.introduction = request.getIntroduction();
  }

  public void updatePassword(String newPassword) {
    this.password = newPassword;
  }
}
