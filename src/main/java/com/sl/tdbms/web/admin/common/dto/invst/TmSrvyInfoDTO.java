package com.sl.tdbms.web.admin.common.dto.invst;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TmSrvyInfoDTO {
	
	private String srvyId; //설문 정보 아이디
    private String srvyTitle; //설문명
    private String srvyType; //조사 종류
    private String srvyTypeNm; //조사 종류명
    private String cstmYn; //커스텀 여부
    private LocalDateTime startDt; //시작 일시
    private LocalDateTime endDt; //종료 일시
    private String registId;
    private LocalDateTime registDt;
    
}
