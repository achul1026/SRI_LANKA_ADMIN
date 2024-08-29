package com.sl.tdbms.web.admin.common.dto.statistics;

import java.util.List;

import lombok.Data;

@Data
public class TsVdsRsltStatisticsDTO {
	private List<TsVdsTrfRsltStatisticsInfo> tsVdsTrfRsltStatisticsList;
	private List<TsVdsSpdRsltStatisticsInfo> tsVdsSpdRsltStatisticsList;
	private List<TsAxleRsltStatisticsInfo> tsAxleSpdRsltStatisticsList;
	
	private int totTrfvlm;		// 총 통행량
	private double totAvgSpd;	// 전체 평균 속도
	
	private String[] colorArrays = {"#1c4e80","#ef7758","#a5d8dd","#556B2F","#5F9EA0","#00BFFF","#EE82EE","#FDF5E6","#778899","#E6E6FA"};
	
	@Data
	public static class TsVdsTrfRsltStatisticsInfo{
		private String vhclClsf;	// 차량 종류
		private int trfvlm; 		// 교통량
		private float rate;			// 교통량 백분율
	}
	
	@Data
	public static class TsVdsSpdRsltStatisticsInfo{
		private String vhclClsf;	// 차량 종류
		private float avgspeed;		// 평균 속도
	}
	
	@Data
	public static class TsAxleRsltStatisticsInfo{
		private String axleType;	// 차축구성
		private float rate;			// 비율
	}
}
