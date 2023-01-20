package com.example.deukgeun.trainer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "trainer_user_profile")
public class Profile extends BaseEntity{
	
	@Id
	@Column(name = "trainer_user_id",unique = true, nullable = false)
	private Long trainerUserId;
	
	@Column(length = 100, nullable = false)
	private String path;
	
    @Builder
    public Profile(Long trainerUserId, String path) {
        this.trainerUserId = trainerUserId;
        this.path = path;
    }
}
