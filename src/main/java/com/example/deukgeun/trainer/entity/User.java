package com.example.deukgeun.trainer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "trainer_user")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String password;
    
    @Column(length = 50, nullable = false, name="profile_image")
    private String profileImage;
    
    @Column(name="group_status")
    @Enumerated(EnumType.STRING)
    private GroupStatus groupStatus;
    
    @Column(length = 50, nullable = false)
    private String groupName;
    
    //빌더
    @Builder
    public User(String name, String email ,String password, String profileImage, GroupStatus groupStatus, String groupName) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.groupStatus = groupStatus;
        this.groupName = groupName;
    }
}
