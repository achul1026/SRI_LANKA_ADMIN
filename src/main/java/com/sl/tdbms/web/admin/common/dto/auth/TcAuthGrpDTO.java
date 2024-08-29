package com.sl.tdbms.web.admin.common.dto.auth;

import java.util.List;

import lombok.Data;

@Data
public class TcAuthGrpDTO {
	
	private String authgrpId;
	
	private String authgrpNm;
	
	private String authgrpDescr;
	
	private String bffltdNm;
	
	private String bscauth_yn;
	
	private List<TcMenuAuthDTO> tcMenuAuthList;
	
	@Data
	public static class TcMenuAuthDTO {
		private String menuauthId;
		private String authgrpId;
		private String menuId;
		private String inputYn = "N";
		private String srchYn 	= "N";
		private String updtYn = "N";
		private String delYn = "N";
	}
	
}
