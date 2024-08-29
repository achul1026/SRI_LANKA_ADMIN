package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;

import lombok.Getter;

@Getter
public enum ColorTypeCd implements CommonEnumType<String> {
	
	RED("SBC001","#FFC2EE"),
	LIGHT_SKY_BLUE("SBC002","#ABDEE6"),
	PURPLE("SBC003","#FAD8FA"),
	YELLOW("SBC004","#F6EAC2"),
	LIGHT_ORANGE("SBC005","#FFCCB6"),
	PINK("SBC006","#FFAEAE"),
	ORANGE("SBC007","#FCD4A6"),
	DARK_BLUE("SBC008","#CCCFFF"),
	SKY_BLUE("SBC009","#A9DBFF"),
	GREEN("SBC010","#A6FCB2"),
	;
	
	private String code; 
	private String name;

	ColorTypeCd(String code, String name) {
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
	
	public static class Converter extends EnumConverter<ColorTypeCd, String> {
        public Converter() {
            super(ColorTypeCd.class);
        }
    }
}
