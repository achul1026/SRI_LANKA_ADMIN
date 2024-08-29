package com.sl.tdbms.web.admin.common.dto.gis;

import lombok.Data;

@Data
public class StatisticsGISFixedMetroCountDTO {

	private String instllcId;
	private String instllcNm;
	private String lat;
	private String lon;
	private String location;
	private String totalCnt;
	private String totalAvg;
	private String laneCnt;
}

