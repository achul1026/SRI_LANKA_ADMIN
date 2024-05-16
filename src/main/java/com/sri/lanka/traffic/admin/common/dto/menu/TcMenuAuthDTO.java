package com.sri.lanka.traffic.admin.common.dto.menu;

import java.math.BigDecimal;
import java.util.List;

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
	
}
