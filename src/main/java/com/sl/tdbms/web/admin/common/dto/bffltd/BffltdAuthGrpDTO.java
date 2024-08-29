package com.sl.tdbms.web.admin.common.dto.bffltd;

import java.util.List;

import lombok.Data;

@Data
public class BffltdAuthGrpDTO {
	
	private String bffltdCd;
	private String bffltdNm;
	
	private List<AuthGrpInfo> subAuthGrpList;
	
	@Data
	public static class AuthGrpInfo {
		private String authgrpId;
		private String authgrpNm;
	}
}
