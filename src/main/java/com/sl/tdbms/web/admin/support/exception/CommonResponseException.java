package com.sl.tdbms.web.admin.support.exception;

import lombok.Getter;

@Getter
public class CommonResponseException extends RuntimeException{

	private ErrorCode errorCode;
	private String message;

	public CommonResponseException() {
		super();
	}
	
	public CommonResponseException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
	}

	public CommonResponseException(ErrorCode errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public CommonResponseException(ErrorCode errorCode, String[] args) {
		super();
		this.errorCode = errorCode;
		this.message = errorCode.getMessage(args);
	}

	
}
