package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum UserTypeCd implements CommonEnumType<String> {
	
	SUPER("UTC001","enums.UserTypeCd.SUPER"),
	GENERAL("UTC002","enums.UserTypeCd.GENERAL");
	
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
		return CommonUtils.getMessage(name);
	}
	
	public static class Converter extends EnumConverter<UserTypeCd, String> {
        public Converter() {
            super(UserTypeCd.class);
        }
    }
}
