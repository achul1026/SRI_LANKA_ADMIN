package com.sri.lanka.traffic.admin.common.enums;

import lombok.Getter;

@Getter
public enum MenuType {

	GENERAL("GENERAL"),
	LIST("LIST"),
	DETAIL("DETAIL"),
	SAVE("SAVE"),
	UPDATE("UPDATE")
	;
	
	private String name;
	
	private MenuType(String name) {
		this.name = name;
	}
	
	
	public static String getCodeByName(String name) {
		for(MenuType r : MenuType.values()) {
			if(r.name.equals(name)) {
				return r.name;
			}
		}
		return null;
	}
}