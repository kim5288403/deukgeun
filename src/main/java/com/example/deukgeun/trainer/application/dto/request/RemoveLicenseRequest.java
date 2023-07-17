package com.example.deukgeun.trainer.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RemoveLicenseRequest {

    @NotNull(message = "삭제하실 자격증을 선택해주세요")
    private List<Long> ids;
}
