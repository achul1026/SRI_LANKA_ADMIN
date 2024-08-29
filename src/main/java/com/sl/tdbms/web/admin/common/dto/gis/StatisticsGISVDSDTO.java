package com.sl.tdbms.web.admin.common.dto.gis;

import lombok.Data;

@Data
public class StatisticsGISVDSDTO {

	private String instllcId;
	private String instllcNm;
	private String cameraId;
	private String lat;
	private String lon;
	private String location;
	private String vhclDrct;
	private String vhclDrctCd;
	private String totalCnt;
	private String totalAvg;
	private String laneCnt;
}

