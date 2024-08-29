package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum ExmnScheduleSttsCd implements CommonEnumType<String> {
	
	NOT_YET_PROGRESS("SSC001","enums.ExmnScheduleSttsCd.NOT_YET_PROGRESS",true),
	NOT_PROGRESS("SSC002","enums.ExmnScheduleSttsCd.NOT_PROGRESS",true),
	NOT_YET_INVESTIGATOR("SSC003","enums.ExmnScheduleSttsCd.NOT_YET_INVESTIGATOR",true),
	PROGRESSING("SSC004","enums.ExmnScheduleSttsCd.PROGRESSING",true),
	PROGRESS_COMPLETE("SSC005","enums.ExmnScheduleSttsCd.PROGRESS_COMPLETE",true),
	NO_MATCH_TYPE("SSC006","enums.ExmnScheduleSttsCd.NO_MATCH_TYPE",false),
	;
	
	private String code; 
	private String name; 
	private boolean isHtmlShow;

	ExmnScheduleSttsCd(String code, String name, boolean isHtmlShow) {
		this.code 		= code;
		this.name 		= name;
		this.isHtmlShow = isHtmlShow;
	}
	
	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return CommonUtils.getMessage(name);
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
