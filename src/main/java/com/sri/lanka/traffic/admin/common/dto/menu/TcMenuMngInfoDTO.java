package com.sri.lanka.traffic.admin.common.dto.menu;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TcMenuMngInfoDTO{
	
	private String menuId;
	private String menuCd;
	private String uppermenuCd;
	private String menuNm;
	private String menuDescr;
	private BigDecimal menuSqno;
	private String uppermenuUrlpttrn;
	private String menuUrlpttrn;
	private String bscmenuYn;
	private String useYn;
	private String currentMenuUrl;
	private String currentMenuNm;
	private String lang;
	private String menuActiveLocation;
	private String subMenuNm;
	private String subMenuUrl;
//	private String parentMenuNm;
//	private String parentMenuUrl;
	
}
