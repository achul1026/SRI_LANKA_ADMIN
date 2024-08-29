package com.sl.tdbms.web.admin.common.dto.mngr;

import com.sl.tdbms.web.admin.common.dto.common.CommonDTO;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;

import lombok.Data;
import lombok.EqualsAndHashCode;
	
@Data
@EqualsAndHashCode(callSuper=true)
public class TcUserAuthMngDTO extends CommonDTO {
	
	private String usermngId;
	private String userId;
	private String userBffltd;
	private String userEmail;
	private AthrztSttsCd athrztStts;
	private String userNm;
	private String userTel;
	private String bffltdNm;
	private String authgrpId;
	private String authgrpNm;
	
}
