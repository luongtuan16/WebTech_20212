package com.socialmedia.response;

public class BaseResponse {
	private String code;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
