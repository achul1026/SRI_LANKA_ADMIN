package com.sri.lanka.traffic.admin.common.enums;

import lombok.Getter;

@Getter
public enum ValidationPattern{
	USERID("^[a-zA-Z0-9]+$", "관리자 아이디"),
	EMAIL("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$", "이메일"),
	PASSWORD("^[a-z\\d$@$!%*#?&]+$", "비밀번호"),
	TEL("^[0-9]+$","연락처"), // TODO 패턴 확인 필요
	NAME("^[a-zA-Z]+$", "이름");
	
	private String pattern;
	private String errorMessage;
	
	ValidationPattern(String pattern, String errorMessage){
		this.pattern = pattern;
		this.errorMessage = errorMessage; 
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
