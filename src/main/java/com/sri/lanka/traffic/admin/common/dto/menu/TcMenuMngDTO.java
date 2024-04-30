package com.sri.lanka.traffic.admin.common.dto.menu;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.lanka.traffic.admin.common.dto.common.CommonDTO;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class TcMenuMngDTO extends CommonDTO {
	
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
	
	private List<SubMenuInfo> subMenuList;
	
	@Data
	public static class SubMenuInfo {
		private String menuId;
		private String menuNm;
		private String menuUrlpttrn;
		private String uppermenuUrlpttrn;
		private String useYn;
		private BigDecimal menuSqno;
		private String uppermenuCd;
		private String menuCd;
	}
	
	public void setJsonSubMenuList(String strSubMenu) {
		if(!CommonUtils.isNull(strSubMenu)) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				List<SubMenuInfo> subMenuList = Arrays.asList(objectMapper.readValue(strSubMenu, SubMenuInfo[].class))
													.stream().filter(x -> !CommonUtils.isNull(x.menuId))
													.sorted(Comparator.comparing(SubMenuInfo::getMenuSqno))
													.collect(Collectors.toList());
				this.subMenuList = subMenuList;
			} catch (JsonProcessingException e) { 
			    this.subMenuList = null;
			}
		}
	}
	
	
	
}
