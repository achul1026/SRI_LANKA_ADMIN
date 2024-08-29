package com.sl.tdbms.web.admin.common.dto.tccdgrp;

import com.sl.tdbms.web.admin.common.dto.common.CommonDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class TcCdInfoDTO extends CommonDTO {
	
	private String cdId; //코드 아이디

    private String grpcdId; //그룹 코드 아이디

    private String cd; //코드

    private String cdNm; //코드 명
    
    private String cdnmEng; //코드 명 eng
    
    private String cdnmKor; //코드 명 kor
    
    private String cdnmSin; //코드 명 sin
    
    private String cdDescr; //코드 설명
    
    private String useYn; //사용 여부
	
}
