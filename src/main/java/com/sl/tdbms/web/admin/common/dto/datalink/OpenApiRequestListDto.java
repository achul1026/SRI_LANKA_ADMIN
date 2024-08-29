package com.sl.tdbms.web.admin.common.dto.datalink;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OpenApiRequestListDto {
	private String certkeyId; //인증 키 아이디
	private String email; //이메일
	private String apiKey; //API 키
	private String endYmd; //종료 일자
	private String sttsCd; //상태 코드
	private LocalDateTime rqstDt; //요청 일시
	private LocalDateTime athrztDt; //승인 일시
	private String athrztId; //승인 아이디
	private String aplyRsn; //신청 사유
	private String srvcId;
	private String srvcNm;
	private String srvcUrl;
	private String srvcClsf;
	private String srvcDescr;
	private String pvsnInst;
	private String cnntYn; //커넥션 되어있는지 컬럼
}
