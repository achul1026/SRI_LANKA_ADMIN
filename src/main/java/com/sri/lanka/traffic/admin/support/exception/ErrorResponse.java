package com.sri.lanka.traffic.admin.support.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
	private int status;
	private String message;
	
	public ErrorResponse(ErrorCode e) {
		this.status = e.getStatus();
		this.message = e.getMessage();
	}
	
}
