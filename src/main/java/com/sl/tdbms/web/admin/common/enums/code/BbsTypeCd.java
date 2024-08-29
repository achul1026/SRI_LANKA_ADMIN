package com.sl.tdbms.web.admin.common.enums.code;

import lombok.Getter;

@Getter
public enum BbsTypeCd {
	
	NOTICE("BTC001"),
	NEWS("BTC002"),
	RESOURCES("BTC003"),
	ALARM("BTC004"),
	;
	
	private String code; 
	
	BbsTypeCd(String code) {
		this.code = code;
	}
	
    public String getCode() {
        return code;
    }
	
}
