package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum AthrztSttsCd implements CommonEnumType<String> {
	
	APPROVAL("ASC001","enums.AthrztSttsCd.APPROVAL"),
	NOT_APPROVED("ASC002","enums.AthrztSttsCd.NOT_APPROVED"),
	SUSPENDED("ASC003","enums.AthrztSttsCd.SUSPENDED");
	
	private String code; 
	private String name;
	
	AthrztSttsCd(String code, String name) {
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
	
	public static AthrztSttsCd getEnums(String code) {
		if(!CommonUtils.isNull(code)) {
			for(AthrztSttsCd r : AthrztSttsCd.values()) {
				if(r.code.equals(code)) {
					return r;
				}
			}
		}
		return null;
	}
	
	public static class Converter extends EnumConverter<AthrztSttsCd, String> {
        public Converter() {
            super(AthrztSttsCd.class);
        }
    }
}
