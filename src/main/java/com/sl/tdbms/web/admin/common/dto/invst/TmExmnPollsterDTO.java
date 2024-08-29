package com.sl.tdbms.web.admin.common.dto.invst;

import com.sl.tdbms.web.admin.common.enums.code.PollsterTypeCd;

import lombok.Data;

@Data
public class TmExmnPollsterDTO {
	//목록 데이터
	private String pollsterId; //조사원 아이디

    private String exmnmngId; //조사 관리 아이디

    private String mngrId; //관리자 아이디

    private String pollsterNm; //조사원 명

    private String pollsterEmail; //조사원 이메일
    
    private PollsterTypeCd pollsterType; //조사원 유형(ex.팀원/팀장)

    private String pollsterTel; //조사원 전화번호

    private String pollsterBrdt; //조사원 생년월일
    
    private String pollsterYn; //조사원 조사여부
	
}
