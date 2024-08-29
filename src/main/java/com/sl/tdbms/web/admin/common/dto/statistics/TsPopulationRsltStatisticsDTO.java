package com.sl.tdbms.web.admin.common.dto.statistics;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TsPopulationRsltStatisticsDTO {

	private String appendId;
	private String chartType;
	private String[] colorArray = {"#DCEDC8","#B3DDCC","#8ACDCE","#62BED2","#46AACE","#3D91BE","#3577AE","#2D5E9E","#24448E","#1C2B7F","#2E1C7F","#401C7F","#631C7F","#7F1C78","#AE3167","#CA3877","#E25A95","#F672AB"};
	private String[] labelArray;
	private List<PopulationRsltStatisticsInfo> statisticsList;
	private List<Map<String,Object>> tableData;

	@Data
	public static class PopulationRsltStatisticsInfo{
		private List<Long> values; //카운트 array
		private List<String> labels; //제목 array
		private BigDecimal[] countArray; //카운트 Array
		private String title;
	}
}
