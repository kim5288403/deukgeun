package com.example.deukgeun.trainer.domain.model.valueobjcet;

import lombok.Getter;

@Getter
public class Group {
    private GroupStatus groupStatus;
    private String groupName;

    public Group(GroupStatus groupStatus, String groupName) {
        this.groupStatus = groupStatus;
        this.groupName = groupName;
    }
}
