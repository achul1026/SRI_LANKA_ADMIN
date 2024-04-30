package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum RqstSttsCd implements CommonEnumType<String> {
	
	APPROVAL("RSC001","승인"),
	WAITING("RSC002","승인 대기"),
	REJECT("RSC003","반려");
	
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
		return name;
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
