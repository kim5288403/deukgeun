package com.example.deukgeun.trainer.domain.model.aggregate;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Trainer {

  private Long id;

  private String name;

  private String email;

  private String password;

  private GroupStatus groupStatus;

  private Gender gender;

  private String groupName;

  private Address address;

  private Integer price;

  private String introduction;

  private List<License> licenses = new ArrayList<>();

  private Profile profile;

  private Post post;

  public Trainer(
          Long id,
          String name,
          String email,
          String password,
          GroupStatus groupStatus,
          String groupName,
          Address address,
          Gender gender,
          Integer price,
          String introduction,
          List<License> license,
          Profile profile,
          Post post
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.groupStatus = groupStatus;
    this.groupName = groupName;
    this.address = address;
    this.gender = gender;
    this.price = price;
    this.introduction = introduction;
    this.licenses = license;
    this.profile = profile;
    this.post = post;
  }

  public Trainer(
          Long id,
          String name,
          String email,
          String password,
          GroupStatus groupStatus,
          String groupName,
          Address address,
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
    this.address = address;
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
          Address address,
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
            address,
            gender,
            price,
            introduction
            );
  }
  public boolean doesPostExist() {
    return this.post != null;
  }
  public void setProfile(Profile profile) {
    this.profile = profile;
  }
  public void setPost(Post post) {
    this.post = post;
  }
  public void updateInfo(UpdateInfoRequest request) {
    this.email = request.getEmail();
    this.name = request.getName();
    this.gender = request.getGender();
    this.address = new Address(
                    request.getPostcode(),
                    request.getJibunAddress(),
                    request.getRoadAddress(),
                    request.getDetailAddress(),
                    request.getExtraAddress()
            );
    this.price = request.getPrice();
    this.groupStatus = request.getGroupStatus();
    this.groupName = request.getGroupName();
    this.introduction = request.getIntroduction();
  }
  public void updatePassword(String newPassword) {
    this.password = newPassword;
  }

}
