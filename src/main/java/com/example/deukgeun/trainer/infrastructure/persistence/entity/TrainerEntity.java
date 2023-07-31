package com.example.deukgeun.trainer.infrastructure.persistence.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Table(name = "trainer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerEntity extends BaseEntity implements UserDetails{

  @Id
  @Column(name = "trainer_id")
  private Long id;

  @Column(length = 50, nullable = false)
  private String name;

  @Column(length = 100, nullable = false)
  private String email;

  @Column(length = 100, nullable = false)
  private String password;

  @Column(name = "group_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private GroupStatus groupStatus;

  @Column(name = "gender", nullable = false)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(length = 50, nullable = false)
  private String groupName;

  @Column(length = 50, nullable = false)
  private String postcode;

  @Column(length = 50)
  private String jibunAddress;

  @Column(length = 50)
  private String roadAddress;

  @Column(length = 50, nullable = false)
  private String detailAddress;

  @Column(length = 50)
  private String extraAddress;

  @Column(length = 50, nullable = false)
  private Integer price;

  @Column(length = 50, nullable = false)
  private String introduction;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "trainer_id", insertable = false, updatable = false, nullable = false)
  private List<LicenseEntity> licenseEntities = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "trainer_id", insertable = false, updatable = false, nullable = false)
  private ProfileEntity profileEntity;

  @OneToOne
  @JoinColumn(name = "trainer_id", insertable = false, updatable = false, nullable = false)
  private PostEntity postEntity;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
