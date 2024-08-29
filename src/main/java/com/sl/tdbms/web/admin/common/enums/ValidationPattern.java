package com.sl.tdbms.web.admin.common.enums;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum ValidationPattern{
	USERID("^[a-zA-Z0-9]+$", "enums.ValidationPattern.USERID"),
	EMAIL("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$", "enums.ValidationPattern.EMAIL"),
	PASSWORD("^[a-z\\d$@$!%*#?&]+$", "enums.ValidationPattern.PASSWORD"),
	TEL("^07\\d{8}$","enums.ValidationPattern.TEL"), // TODO 패턴 확인 필요
	NAME("^[A-Za-z\\s]+$", "enums.ValidationPattern.NAME");
	
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
		this.errorMessage = CommonUtils.getMessage(errorMessage);
	}
	
}
