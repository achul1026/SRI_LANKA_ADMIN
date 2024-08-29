package com.sl.tdbms.web.admin.common.dto.gis;

import lombok.Data;

import java.util.List;

@Data
public class StatisticsGISDetailDTO {

	private List<TableDataInfo> tableDataList;
	private List<LineGraphDataInfo> lineGraphList;
	private List<VhclDrctDataInfo> drctCdList;
	private long totalCnt;

	@Data
	public static class LineGraphDataInfo{
		private String dateInfo;
		private String trfvlm;
		private String avgSpeed;

	}
	@Data
	public static class TableDataInfo{
		private String rnum;
		private String vhclClsf;
		private String trfvlm;
		private String rate;
		private String avgSpeed;

	}

	@Data
	public static class VhclDrctDataInfo{
		private String vhclDrctCd;
		private String vhclDrct;
	}
}
