package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum AthrztSttsCd implements CommonEnumType<String> {
	
	APPROVAL("ASC001","승인"),
	NOT_APPROVED("ASC002","미승인"),
	SUSPENDED("ASC003","정지");
	
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
		return name;
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
