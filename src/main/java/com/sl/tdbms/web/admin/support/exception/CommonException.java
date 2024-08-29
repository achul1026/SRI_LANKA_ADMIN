package com.sl.tdbms.web.admin.support.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{

	private ErrorCode errorCode;
	private String message;

	public CommonException() {
		super();
	}
	
	public CommonException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
	}

	public CommonException(ErrorCode errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public CommonException(ErrorCode errorCode, String[] args) {
		super();
		this.errorCode = errorCode;
		this.message = errorCode.getMessage(args);
	}
	
}
