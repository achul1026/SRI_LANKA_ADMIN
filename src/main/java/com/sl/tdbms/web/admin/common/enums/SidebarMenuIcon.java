package com.sl.tdbms.web.admin.common.enums;

import lombok.Getter;

@Getter
public enum SidebarMenuIcon {

	DATA_MONITORING("MMC001", "/images/sidebar_icon01.png"),
	DATA_MNG("MMC002", "/images/sidebar_icon02.png"),
	DATA_LINK("MMC003", "/images/sidebar_icon03.png"),
	SYSTEM_MNG("MMC004", "/images/sidebar_icon04.png"),
	DATA_STATISTICS("MMC005", "/images/sidebar_icon05.png"),
	PORTAL_MNG("MMC006", "/images/sidebar_icon06.png"),
	;

	private String code;
	private String path;

	private SidebarMenuIcon(String code,String path) {
		this.code = code;
		this.path = path;
	}
	
	
	public static String getPathByCode(String code) {
		for(SidebarMenuIcon r : SidebarMenuIcon.values()) {
			if(r.code.equals(code)) {
				return r.path;
			}
		}
		//default value
		return "/images/sidebar_icon01.png";
	}
}