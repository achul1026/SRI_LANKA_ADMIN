package com.sl.tdbms.web.admin.common.dto.invst;

import java.time.LocalDateTime;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;

@Data
public class TlExmnRsltHistorySearchDTO {
	
	private String searchDate = CommonUtils.formatLocalDateTime(LocalDateTime.now(), "YYYY-MM-dd"); //검색 날짜
	private String exmndrctId; // 조사방향 아이디
	
}
