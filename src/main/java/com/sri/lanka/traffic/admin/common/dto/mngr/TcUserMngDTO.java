package com.sri.lanka.traffic.admin.common.dto.mngr;

import com.sri.lanka.traffic.admin.common.dto.common.CommonDTO;
import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.UserTypeCd;

import lombok.Data;
import lombok.EqualsAndHashCode;
	
@Data
@EqualsAndHashCode(callSuper=true)
public class TcUserMngDTO extends CommonDTO {
	
	private String usermngId;
	private String authId;
	private String userId;
	private String userPswd;
	private String userBffltd;
	private String userDept;
	private String userEmail;
	private UserTypeCd userType;
	private AthrztSttsCd athrztStts;
	private String userNm;
	private String userAuth;
	private String userTel;
	private String authNm;
	private String bffltdNm;
	private String deptNm;
	
}
