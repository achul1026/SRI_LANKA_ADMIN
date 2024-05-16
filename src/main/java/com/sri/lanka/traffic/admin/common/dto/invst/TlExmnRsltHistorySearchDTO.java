package com.sri.lanka.traffic.admin.common.dto.invst;

import java.time.LocalDateTime;

import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;

@Data
public class TlExmnRsltHistorySearchDTO {
	
    private String exmnrsltId; //조사 결과 아이디
    private String lcchgRsn; //위치 변경 사유
	
	private String searchDate = CommonUtils.formatLocalDateTime(LocalDateTime.now(), "YYYY-MM-dd"); //검색 날짜
	private String exmndrctId; // 조사방향 아이디
	
}
