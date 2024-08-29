package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum PollsterTypeCd implements CommonEnumType<String> {
	
	MEMBER("PTC001","enums.PollsterTypeCd.MEMBER"),
	LEADER("PTC002","enums.PollsterTypeCd.LEADER");
	
	private String code; 
	private String name;
	
	PollsterTypeCd(String code, String name) {
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
	
	public static class Converter extends EnumConverter<PollsterTypeCd, String> {
        public Converter() {
            super(PollsterTypeCd.class);
        }
    }
}
