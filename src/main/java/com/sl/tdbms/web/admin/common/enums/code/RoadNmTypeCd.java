package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum RoadNmTypeCd implements CommonEnumType<String> {
	
	CORDON_LINE("RTC001","enums.LocationPointTypeCd.CORDON_LINE","cordonLine"),
	SCREEN_LINE("RTC003","enums.RoadNmTypeCd.SCREEN_LINE","screenLine");
	
	private String code; 
	private String name;
	private String column;
	
	RoadNmTypeCd(String code, String name, String column) {
		this.code = code;
		this.name = name;
		this.column = column;
	}
	
	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return CommonUtils.getMessage(name);
//		return name;
	}
	
	public String getColumn() {
		return column;
	}
	
	public static class Converter extends EnumConverter<RoadNmTypeCd, String> {
        public Converter() {
            super(RoadNmTypeCd.class);
        }
    }
}
