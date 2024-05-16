package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;

import lombok.Getter;

@Getter
public enum BbsTypeCd implements CommonEnumType<String> {
	
	NOTICE("BTC001","공지사항"),
	ALARM("BTC002","알림"),
	REPORT("BTC003","보고서"),
	;
	
	private String code; 
	private String name;
	
	BbsTypeCd(String code, String name) {
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
	
}
