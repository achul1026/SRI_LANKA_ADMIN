package com.sl.tdbms.web.admin.common.dto.invst;

import java.time.LocalDateTime;
import java.util.List;

import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.Data;

@Data
public class TlExmnSrvyRsltHistoryDTO {
	
	private String exmnmngId; //조사아이디
    private String exmnNm; //조사명
    private ExmnTypeCd exmnType; //조사타입
    private LocalDateTime startDt; //시작 일시
    private LocalDateTime endDt; //종료 일시
	private int invstGoalCnt = 0;//목표 횟수
	private int invstCompletedCnt = 0;//조사 완료
	private int invstIncompletedCnt = 0;//미달성 횟수
	private List<SurveyHistoryTableInfo> surveyHistoryTableList;
	private int surveyHistoryTotalCnt;
	private PagingUtils paging;
	
	@Data
	public static class SurveyHistoryTableInfo{
//		private String invstDt; 	//진행 조사 날짜
//		private int completeCnt = 0;	//조사 완료 횟수
//		private int accumulateCnt = 0;	//누적 횟수
		private String startDt;
		private String pollsterNm;
		private String pollsterTel;
		private String registDt;
		private String srvyrsltId;
		private String ansCnts;
	}
	
}
