package com.sl.tdbms.web.admin.common.dto.menu;

import java.math.BigDecimal;
import java.util.List;

import com.sl.tdbms.web.admin.common.enums.SidebarMenuIcon;

import lombok.Data;

@Data
public class TcMenuAuthDTO {
	
	private String authgrpId;
	private String menuId;
	private String menuCd;
	private String uppermenuCd;
	private String menuNm;
	private String menuDescr;
	private String uppermenuUrlpttrn;
	private String menuUrlpttrn;

	private String iconPath;
	
	private List<SubAuthMenuInfo> subMenuList;
	
	@Data
	public static class SubAuthMenuInfo {
		private String menuId;
		private String menuCd;
		private String uppermenuCd;
		private BigDecimal menuSqno;
		private String menuNm;
		private String uppermenuUrlpttrn;
		private String menuUrlpttrn;
	}
	
	public void setMenuCd(String menuCd){
		this.menuCd = menuCd;
		this.iconPath = SidebarMenuIcon.getPathByCode(menuCd);
	}
}
