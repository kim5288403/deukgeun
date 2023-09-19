package com.example.deukgeun.global.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestResponse {
	private String status;
	private int code;
	private String message;
	private Object data;
}
