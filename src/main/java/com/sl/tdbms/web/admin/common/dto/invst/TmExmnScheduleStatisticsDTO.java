package com.sl.tdbms.web.admin.common.dto.invst;

import lombok.Data;

@Data
public class TmExmnScheduleStatisticsDTO {
	
	private StatisticsMonthInfo statisticsMonthInfo; 
	private StatisticsTodayInfo statisticsTodayInfo; 
	
	@Data
	public static class StatisticsMonthInfo {
		private int notYetProgressCnt		= 0; 	//진행 예정
		private int notProgressCnt			= 0;	//미진행
		private int progressCompleteCnt		= 0; 	//진행 완료
		private int progressingCnt			= 0; 	//진행중
		private int notYetInvestigatorCnt	= 0;	//조사원 미등록
	}
	
	@Data
	public static class StatisticsTodayInfo {
		private int notYetProgressCnt		= 0;	//진행 예정
		private int notProgressCnt			= 0;	//미진행
		private int progressCompleteCnt		= 0; 	//진행 완료
		private int progressingCnt			= 0; 	//진행중
		private int notYetInvestigatorCnt	= 0; 	//조사원 미등록
	}
	
}
