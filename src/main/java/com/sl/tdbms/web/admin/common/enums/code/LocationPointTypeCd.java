package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum LocationPointTypeCd implements CommonEnumType<String> {
	
	CORDON_LINE("RTC001","enums.LocationPointTypeCd.CORDON_LINE","cordonLine"),
	TOLL_BOOTH("RTC002","enums.LocationPointTypeCd.TOLL_BOOTH","tollBooth");
	
	private String code; 
	private String name;
	private String column;
	
	LocationPointTypeCd(String code, String name, String column) {
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
	}
	
	public String getColumn() {
		return column;
	}
	
	public static class Converter extends EnumConverter<LocationPointTypeCd, String> {
        public Converter() {
            super(LocationPointTypeCd.class);
        }
    }
}
