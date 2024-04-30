package com.sri.lanka.traffic.admin.common.dto.auth;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.lanka.traffic.admin.common.dto.common.CommonDTO;
import com.sri.lanka.traffic.admin.common.entity.TcAuthMng;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class TcAuthMngDetailDTO extends CommonDTO {

	private TcAuthMng tcAuthMng;
	private List<TcMenuAuthInfo> authMenuList;

	@Data
	public static class TcMenuAuthInfo {
		private String menuId;
		private String menuCd;
		private String uppermenuCd;
		private String menuNm;
		private String menuauthId;
		private String menuAuthCheck = "N";
		private List<SubAuthMenuInfo> subAuthMenuList;

		@Data
		public static class SubAuthMenuInfo {
			private String menuId;
			private String menuCd;
			private String uppermenuCd;
			private BigDecimal menuSqno;
			private String menuNm;
			private String inputYn = "N";
			private String srchYn 	= "N";
			private String updtYn = "N";
			private String delYn = "N";
			private String menuauthId;

		}
		public void setJsonSubMenuList(String strSubMenu) {
			if(!CommonUtils.isNull(strSubMenu)) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<SubAuthMenuInfo> subAuthMenuList = Arrays.asList(objectMapper.readValue(strSubMenu, SubAuthMenuInfo[].class))
							.stream()
							.filter(x -> !CommonUtils.isNull(x.menuId))
							.sorted(Comparator.comparing(SubAuthMenuInfo::getMenuSqno))
							.collect(Collectors.toList());
					this.subAuthMenuList = subAuthMenuList;
				} catch (JsonProcessingException e) {
					this.subAuthMenuList = null;
				}
			}
		}
		public void setSubMenuCheckedVal(String subMenuCheckedVal) {
			if(!CommonUtils.isNull(subMenuCheckedVal)) {
				if(subMenuCheckedVal.contains("Y")) {
					this.menuAuthCheck = "Y";
				}
			}
		}
	}
	
}
