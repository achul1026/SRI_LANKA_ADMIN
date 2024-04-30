package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum ExmnScheduleSttsCd implements CommonEnumType<String> {
	
	NOT_YET_PROGRESS("SSC001","진행예정",true),
	NOT_PROGRESS("SSC002","미진행",true),
	NOT_YET_INVESTIGATOR("SSC003","조사원 미등록",true),
	PROGRESSING("SSC004","진행중",true),
	PROGRESS_COMPLETE("SSC005","진행완료",true),
	NO_MATCH_TYPE("SSC006","noMathType",false),
	;
	
	private String code; 
	private String name;
	private boolean isHtmlShow;

	ExmnScheduleSttsCd(String code, String name, boolean isHtmlShow) {
		this.code = code;
		this.name = name;
		this.isHtmlShow = isHtmlShow;
	}
	
	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return name;
	}
	
	public static class Converter extends EnumConverter<ExmnScheduleSttsCd, String> {
        public Converter() {
            super(ExmnScheduleSttsCd.class);
        }
    }
	
	public static ExmnScheduleSttsCd getEnums(String code) {
		if(!CommonUtils.isNull(code)) {
			for(ExmnScheduleSttsCd r : ExmnScheduleSttsCd.values()) {
				if(r.code.equals(code)) {
					return r;
				}
			}
		}
		return null;
	}
}
