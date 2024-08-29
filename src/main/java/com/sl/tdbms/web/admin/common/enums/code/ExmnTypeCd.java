package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import lombok.Getter;

@Getter
public enum ExmnTypeCd implements CommonEnumType<String> {
	
	MCC("ETC001","enums.ExmnTypeCd.MCC","traffic","true"),
	TM("ETC002","enums.ExmnTypeCd.TM","traffic","true"),
	ROADSIDE("ETC003","enums.ExmnTypeCd.ROADSIDE","survey","false"),
	OD("ETC004","enums.ExmnTypeCd.OD","survey","false"),
	AXLELOAD("ETC005","enums.ExmnTypeCd.AXLELOAD","survey","true"),
	;
	
	private String code; 
	private String name;
	private String type;
	private String hasDrct;
	
	ExmnTypeCd(String code, String name, String type, String hasDrct) {
		this.code 	= code;
		this.name 	= name;
		this.type 	= type;
		this.hasDrct = hasDrct;
	}
	
	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return CommonUtils.getMessage(name);
	}
	
	public static class Converter extends EnumConverter<ExmnTypeCd, String> {
        public Converter() {
            super(ExmnTypeCd.class);
        }
    }

	public static ExmnTypeCd getEnums(String code) {
		if(!CommonUtils.isNull(code)) {
			for(ExmnTypeCd r : ExmnTypeCd.values()) {
				if(r.code.equals(code)) {
					return r;
				}
			}
		}
		return null;
	}
}
