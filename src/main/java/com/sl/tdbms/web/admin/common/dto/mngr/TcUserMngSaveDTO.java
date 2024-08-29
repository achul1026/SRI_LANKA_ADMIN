package com.sl.tdbms.web.admin.common.dto.mngr;

import com.sl.tdbms.web.admin.common.dto.common.CommonDTO;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;

import lombok.Data;
import lombok.EqualsAndHashCode;
	
@Data
@EqualsAndHashCode(callSuper=true)
public class TcUserMngSaveDTO extends CommonDTO {
	
	private String userId;
	private String userPswd;
	private String newUserPswd;
	private String authgrpId;
//	private String userBffltd;
	private String userDept;
	private String userEmail;
	private AthrztSttsCd athrztStts;
	private String athrztSttsCd;
	private String userNm;
	private String userTel;
	
}
