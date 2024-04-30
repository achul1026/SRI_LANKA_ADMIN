package com.sri.lanka.traffic.admin.common.dto.common;

import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.UserTypeCd;

import lombok.Data;

@Data
public class LoginMngrDTO {
	private String usermngId;
	private String authId;
	private String userId;
	private String userPswd;
	private String userAuth;
	private UserTypeCd userType;
	private AthrztSttsCd athrztStts;
	private String userNm;
}
