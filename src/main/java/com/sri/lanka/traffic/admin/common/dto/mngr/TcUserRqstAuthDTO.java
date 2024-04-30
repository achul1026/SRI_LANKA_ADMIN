package com.sri.lanka.traffic.admin.common.dto.mngr;

import java.time.LocalDateTime;

import com.sri.lanka.traffic.admin.common.dto.common.CommonDTO;
import com.sri.lanka.traffic.admin.common.enums.code.RqstSttsCd;

import lombok.Data;
import lombok.EqualsAndHashCode;
	
@Data
@EqualsAndHashCode(callSuper=true)
public class TcUserRqstAuthDTO extends CommonDTO {
	
	private String usermngId;
	private String authId;
	private String userId;
	private String userBffltd;
	private String userEmail;
	private String userNm;
	private String userTel;
	private String authNm;
	private String bffltdNm;
	private String authgrpNm;
	private String authrqstId; //권한요청 아이디
    private String authgrpId; //권한그룹 아이디
    private String rqstRsn; //요청 사유
    private LocalDateTime rqstDt; //요청 일시
    private RqstSttsCd rqstStts; //요청 상태
    private LocalDateTime athrztDt; //승인 일시
	
}
