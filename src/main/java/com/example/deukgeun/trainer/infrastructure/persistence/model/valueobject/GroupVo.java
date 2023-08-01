package com.example.deukgeun.trainer.infrastructure.persistence.model.valueobject;

import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupVo {
    @Column(name = "group_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupStatus groupStatus;

    @Column(length = 50, nullable = false)
    private String groupName;
}
