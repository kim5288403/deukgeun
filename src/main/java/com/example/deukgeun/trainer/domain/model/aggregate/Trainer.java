package com.example.deukgeun.trainer.domain.model.aggregate;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import com.example.deukgeun.trainer.domain.dto.UpdateInfoDTO;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Trainer {

  private Long id;

  private Long profileId;

  private Long postId;

  private String name;

  private String email;

  private String password;

  private Gender gender;

  private Group group;

  private Address address;

  private Integer price;

  private String introduction;

  private List<License> licenses;

  private Profile profile;

  private Post post;

  public static Trainer create(
          String name,
          String email,
          String password,
          Group group,
          Address address,
          Gender gender,
          Integer price,
          String introduction
  ) {
    Long id = LongIdGeneratorUtil.gen();
    return new Trainer(
            id,
            null,
            null,
            name,
            email,
            password,
            gender,
            group,
            address,
            price,
            introduction,
            new ArrayList<>(),
            null,
            null
            );
  }

  public void setLicenses(License license) {
    this.licenses.add(license);
  }
  public void setProfile(Profile profile) {
    this.profile = profile;
    if (profile == null) {
      this.profileId = null;
    } else {
      this.profileId = profile.getId();
    }
  }
  public void setPost(Post post) {
    this.post = post;
    if (post == null) {
      this.postId = null;
    } else {
      this.postId = post.getId();
    }
  }
  public void updateInfo(UpdateInfoDTO updateInfoDTO) {
    this.email = updateInfoDTO.getEmail();
    this.name = updateInfoDTO.getName();
    this.gender = updateInfoDTO.getGender();
    this.address = new Address(
            updateInfoDTO.getPostcode(),
            updateInfoDTO.getJibunAddress(),
            updateInfoDTO.getRoadAddress(),
            updateInfoDTO.getDetailAddress(),
            updateInfoDTO.getExtraAddress()
            );
    this.price = updateInfoDTO.getPrice();
    this.group = new Group(
            updateInfoDTO.getGroupStatus(),
            updateInfoDTO.getGroupName()
    );
    this.introduction = updateInfoDTO.getIntroduction();
  }

  public void updatePassword(String newPassword) {
    this.password = newPassword;
  }

}
