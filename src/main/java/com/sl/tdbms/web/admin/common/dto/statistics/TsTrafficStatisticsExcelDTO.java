package com.sl.tdbms.web.admin.common.dto.statistics;

import java.util.List;

import lombok.Data;

@Data
public class TsTrafficStatisticsExcelDTO {
	private VehicleInfoSheet vehicleInfoSheet;
	private List<TimeInfoSheet> timeInfoSheet;
	
	// 갑지 정보
	@Data
	public static class VehicleInfoSheet{
		private String pointNm;			// 지점
		private float lat;				// x좌표
		private float lon;				// y좌표
		private String laneCnt;			// 차로수
		private float avgSpeed;			// 통행속도
		private VehicleListSheet vehicleListSheet;
		
		@Data
		public static class VehicleListSheet{
			private String headerArr;		// 차종 헤더값
			private String trfvlmArr;		// 차종별 통행량
		}
	}
	
	
	// 시간대별 통행량
	@Data
	public static class TimeInfoSheet{
		private String pointNm;		// 지점
		private int statsYy;		// 연도
		private int statsMm;		// 월
		private int statsDd;		// 일
		private String dayOfWeek;	// 요일
		private String drct;		// 방향
		private TimeListSheet timeListSheet;
		
		@Data
		public static class TimeListSheet{
			private String headerArr;	// 시간 헤더값
			private String trfvlmArr;	// 시간대별 통행량
		}
	}
	
}
