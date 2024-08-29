package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum UserSttsCd implements CommonEnumType<String> {
	
	APPROVAL("USC001","enums.UserSttsCd.APPROVAL"),
	NOT_APPROVED("USC002","enums.UserSttsCd.NOT_APPROVED"),
	SUSPENDED("USC003","enums.UserSttsCd.SUSPENDED"),
	RESIGN("USC004","enums.UserSttsCd.RESIGN");
	
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
		return CommonUtils.getMessage(name);
	}
	
	public static class Converter extends EnumConverter<UserSttsCd, String> {
        public Converter() {
            super(UserSttsCd.class);
        }
    }
}
