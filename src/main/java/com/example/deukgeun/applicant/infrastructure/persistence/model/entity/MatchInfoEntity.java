package com.example.deukgeun.applicant.infrastructure.persistence.model.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "match_info")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchInfoEntity extends BaseEntity {
    @Id
    @Column(name = "match_info_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private Integer status;
}
