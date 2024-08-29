package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum ExmnSttsCd implements CommonEnumType<String> {
	
	INVEST_WRITING("ESC001","enums.ExmnSttsCd.INVEST_WRITING","notYetProgress"),
	INVEST_WRITE_COMPLETED("ESC002","enums.ExmnSttsCd.INVEST_WRITE_COMPLETED","notYetProgress"),

	INVEST_PROGRESS("ESC003","enums.ExmnSttsCd.INVEST_PROGRESS","progress"),
	INVEST_COMPLETE("ESC004","enums.ExmnSttsCd.INVEST_COMPLETE","progressComplete"),
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
		return CommonUtils.getMessage(name);
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
