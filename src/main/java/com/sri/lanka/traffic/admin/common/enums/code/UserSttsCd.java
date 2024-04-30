package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;

import lombok.Getter;

@Getter
public enum UserSttsCd implements CommonEnumType<String> {
	
	APPROVAL("USC001","승인"),
	NOT_APPROVED("USC002","미승인"),
	SUSPENDED("USC003","정지"),
	RESIGN("USC004","탈퇴");
	
	private String code; 
	private String name;
	
	UserSttsCd(String code, String name) {
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
	
	public static class Converter extends EnumConverter<UserSttsCd, String> {
        public Converter() {
            super(UserSttsCd.class);
        }
    }
}
