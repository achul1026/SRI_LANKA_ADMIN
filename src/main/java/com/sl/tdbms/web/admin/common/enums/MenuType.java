package com.sl.tdbms.web.admin.common.enums;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum MenuType {

	GENERAL("enums.MenuType.GENERAL"),
	LIST("enums.MenuType.LIST"),
	DETAIL("enums.MenuType.DETAIL"),
	SAVE("enums.MenuType.SAVE"),
	UPDATE("enums.MenuType.UPDATE")
	;
	
	private String name;
	
	private MenuType(String name) {
		this.name = name;
	}
	
//	private String eng;
//	private String kor;
//	private String sin;
//	
//	private MenuType(String eng, String kor, String sin) {
//		this.eng = eng;
//		this.kor = kor;
//		this.sin = sin;
//	}
//	
//	
	public static String getCodeByName(MenuType menuType) {
		for(MenuType r : MenuType.values()) {
			if (r.equals(menuType)) {
				return CommonUtils.getMessage(r.name);
//				if 		(lang.equals("eng")) return r.eng;
//				else if (lang.equals("kor")) return r.kor;
//				else if (lang.equals("sin")) return r.sin;
//				else 						 return r.eng;
			}
		}
		return null;
	}
}