package com.example.deukgeun.commom.response;



import lombok.Builder;
import lombok.Data;

@Data
public class Message {

	private StatusEnum status;
	private String message;
	private Object data;
	
	@Builder
	public Message(StatusEnum status, String message, Object data) {
		this.status = status;
		this.data = data;
		this.message = message;
	}
}
