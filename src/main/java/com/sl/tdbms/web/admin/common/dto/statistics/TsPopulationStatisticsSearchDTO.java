package com.sl.tdbms.web.admin.common.dto.statistics;

import java.util.List;

import com.sl.tdbms.web.admin.common.enums.code.PopStatsTypeCd;

import lombok.Data;

@Data
public class TsPopulationStatisticsSearchDTO {
	private PopStatsTypeCd popStatTypeCd;
	private String bffltdCd;
	private String searchYear;
	
	private String province;
	private String district;
	private String dsDivision;
	private String gnDivision;
	
	private String orderDirection = "ASC";
	
	private List<String> popmngIdArr;
}
