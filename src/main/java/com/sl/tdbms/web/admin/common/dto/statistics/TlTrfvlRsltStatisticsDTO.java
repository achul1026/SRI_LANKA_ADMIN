package com.sl.tdbms.web.admin.common.dto.statistics;

import java.util.List;

import lombok.Data;

@Data
public class TlTrfvlRsltStatisticsDTO {


	private List<DirectionInfo> directionList;
	private List<TlTrfvlRsltStatisticsInfo> tlTrfvlRsltStatisticsList;

	private String[] colorArrays = {"#1c4e80","#ef7758","#a5d8dd","#556B2F","#5F9EA0","#00BFFF","#EE82EE","#FDF5E6","#778899","#E6E6FA"};

	@Data
	public static class DirectionInfo{

		private String 	startlcNm; //시작위치 명
		private String 	endlcNm; //종료위치 명

	}
	@Data
	public static class TlTrfvlRsltStatisticsInfo{

		private String 	vhclClsf; //이동 수단 유형
		private String 	name; //이동 수단 유형명
		private int 	value; //이동 수단 교통량
		private float 	rate; //이동 수단 교통량 백분율
	}

}
