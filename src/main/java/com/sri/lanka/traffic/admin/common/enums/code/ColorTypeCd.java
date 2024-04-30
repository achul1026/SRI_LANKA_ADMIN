package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;

import lombok.Getter;

@Getter
public enum ColorTypeCd implements CommonEnumType<String> {
	
	RED("SBC001","#ff968a"),
	LIGHT_SKY_BLUE("SBC002","#abdee6"),
	PURPLE("SBC003","#cbaacb"),
	YELLOW("SBC004","#f6eac2"),
	LIGHT_ORANGE("SBC005","#ffccb6"),
	PINK("SBC006","#f3b0c3"),
	ORANGE("SBC007","#e99f48"),
	DARK_BLUE("SBC008","#8a92ff"),
	SKY_BLUE("SBC009","#699bed"),
	GREEN("SBC010","#82df8f"),
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
