package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;

import lombok.Getter;

@Getter
public enum UserTypeCd implements CommonEnumType<String> {
	
	SUPER("UTC001","최고 관리자"),
	GENERAL("UTC002","일반 관리자");
	
	private String code; 
	private String name;
	
	UserTypeCd(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return name;
	}
	
	public static class Converter extends EnumConverter<UserTypeCd, String> {
        public Converter() {
            super(UserTypeCd.class);
        }
    }
}
