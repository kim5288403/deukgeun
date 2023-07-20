package com.example.deukgeun.authMail.application.dto.response;

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
