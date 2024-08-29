package com.sl.tdbms.web.admin.common.dto.statistics;

import lombok.Data;

@Data
public class TsTrafficStatisticsSearchDTO {
	private String surveyType;	// 조사 유형
	private String facltId;		// 시설물 아이디
	private String siteId;		// 장소 아이디
	private String searchDate;	// 검색일자
	
	private String grpCdId = "5dc28b36c8d94073b4eb827a4aae4a2d";	
}
