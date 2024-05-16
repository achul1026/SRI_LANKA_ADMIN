package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import lombok.Getter;

@Getter
public enum ExmnTypeCd implements CommonEnumType<String> {
	
	MCC("ETC001","MCC 조사", "traffic","true"),
	TM("ETC002","TM 조사", "traffic","true"),
	LABORSIDE("ETC003","노측 면접 조사", "survey","false"),
	OD("ETC004","OD 조사", "survey","false"),
	AXLELOAD("ETC005","AXLELOAD 조사", "survey","true"),
	;
	
	private String code; 
	private String name;
	private String type;
	private String hasDrct;
	
	ExmnTypeCd(String code, String name, String type, String hasDrct) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.hasDrct = hasDrct;
	}
	
	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return name;
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
