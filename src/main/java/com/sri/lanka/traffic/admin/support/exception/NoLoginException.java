package com.sri.lanka.traffic.admin.support.exception;

import lombok.Getter;

@Getter
public class NoLoginException extends RuntimeException{
	private ErrorCode errorCode;
	private String message;

	public NoLoginException() {
		super();
		this.errorCode = ErrorCode.NOT_FOUND_USER_INFO;
		this.message = ErrorCode.NOT_FOUND_USER_INFO.getMessage();
	}
	
	public NoLoginException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
	}

	public NoLoginException(ErrorCode errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
}
