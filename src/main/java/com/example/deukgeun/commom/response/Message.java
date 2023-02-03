package com.example.deukgeun.commom.response;




import lombok.Builder;
import lombok.Data;

@Data
public class Message {

	private String status;
	private int code;
	private String message;
	private Object data;
	
	@Builder
	public Message(int code ,String status, String message, Object data) {
		this.code = code;
		this.status = status;
		this.data = data;
		this.message = message;
	}
}
