package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum ExmnSttsCd implements CommonEnumType<String> {
	
	INVEST_WRITING("ESC001","작성중", "notYetProgress"),
	INVEST_WRITE_COMPLETED("ESC002","작성 완료", "notYetProgress"),

	INVEST_PROGRESS("ESC003","조사 시작" , "progress"),
	INVEST_COMPLETE("ESC004","조사 완료" , "progressComplete"),
	;
	
	private String code; 
	private String name;

	private String status;
	ExmnSttsCd(String code, String name, String status) {
		this.code = code;
		this.name = name;
		this.status = status;
	}
	
	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return name;
	}
	
	public static class Converter extends EnumConverter<ExmnSttsCd, String> {
        public Converter() {
            super(ExmnSttsCd.class);
        }
    }
	
	public static ExmnSttsCd getEnums(String code) {
		if(!CommonUtils.isNull(code)) {
			for(ExmnSttsCd r : ExmnSttsCd.values()) {
				if(r.code.equals(code)) {
					return r;
				}
			}
		}
		return null;
	}
}
