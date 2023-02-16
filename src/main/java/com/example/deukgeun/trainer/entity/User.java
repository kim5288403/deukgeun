package com.example.deukgeun.trainer.entity;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "trainer_user")
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails{

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
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

  @Column(length = 50, nullable = false)
  private String groupName;
  
  @Column(length = 50, nullable = false)
  private String postcode;
  
  @Column(length = 50, nullable = false)
  private String jibunAddress;
  
  @Column(length = 50, nullable = false)
  private String roadAddress;
  
  @Column(length = 50, nullable = false)
  private String detailAddress;
  
  @Column(length = 50, nullable = false)
  private String extraAddress;
  
  @Column(name = "profile_id")
  private Long profileId;
  
  @OneToOne
  @JoinColumn(name = "profile_id", insertable = false, updatable = false, nullable = false)
  private Profile profile;

  @Builder
  public User(String name, String email, String password, GroupStatus groupStatus,
      String groupName, String postcode, String jibunAddress, String roadAddress, String detailAddress, String extraAddress, Long profileId) {
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
    this.profileId = profileId;
  }

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
