package com.sl.tdbms.web.admin.common.enums;

import lombok.Getter;

@Getter
public enum AuthType {

	GENERAL("GENERAL"),
	CREATE("CREATE"),
	READ("READ"),
	UPDATE("UPDATE"),
	DELETE("DELETE")
	;
	
	private String name;
	
	private AuthType(String name) {
		this.name = name;
	}
	
	
	public static String getCodeByName(String name) {
		for(AuthType r : AuthType.values()) {
			if(r.name.equals(name)) {
				return r.name;
			}
		}
		return null;
	}
}