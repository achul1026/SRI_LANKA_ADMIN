package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum RqstSttsCd implements CommonEnumType<String> {
	
	APPROVAL("RSC001","enums.RqstSttsCd.APPROVAL"),
	WAITING("RSC002","enums.RqstSttsCd.WAITING"),
	REJECT("RSC003","enums.RqstSttsCd.REJECT");
	
	private String code; 
	private String name;
	
	RqstSttsCd(String code, String name) {
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
	
	public static RqstSttsCd getEnums(String code) {
		if(!CommonUtils.isNull(code)) {
			for(RqstSttsCd r : RqstSttsCd.values()) {
				if(r.code.equals(code)) {
					return r;
				}
			}
		}
		return null;
	}
	
	public static class Converter extends EnumConverter<RqstSttsCd, String> {
        public Converter() {
            super(RqstSttsCd.class);
        }
    }
}
