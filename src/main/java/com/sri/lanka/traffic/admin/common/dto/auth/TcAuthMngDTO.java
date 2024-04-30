package com.sri.lanka.traffic.admin.common.dto.auth;

import java.util.List;

import com.sri.lanka.traffic.admin.common.dto.common.CommonDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class TcAuthMngDTO extends CommonDTO {
	
	private String authId;
	private String authNm;
	private String authDescr;
	
	private List<TcMenuAuthDTO> tcMenuAuthList;
	
	@Data
	public static class TcMenuAuthDTO {
		private String menuauthId;
		private String authId;
		private String menuId;
		private String inputYn = "N";
		private String srchYn 	= "N";
		private String updtYn = "N";
		private String delYn = "N";
	}
}
