package com.sri.lanka.traffic.admin.common.dto.invst;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.sri.lanka.traffic.admin.common.util.PagingUtils;

import lombok.Data;

@Data
public class TlExmnRsltHistoryDTO {
	
    private String lcchgRsn; //위치 변경 사유
    private LocalDateTime startDt; //시작 일시
    private LocalDateTime endDt; //종료 일시
	private String exmnDiv; //조사 근무 형태
	private int invstTotalCnt;//총 조사 횟수
	private int invstCompletedCnt;//조사 완료
	private int invstNotCompletedCnt;//조사 미완료
	private List<Map<String,Object>> hourList;
	private List<TrafficHistoryTableInfo> trafficHistoryTableList;
	private int trafficHistoryTotalCnt;
	private PagingUtils paging;
	
	@Data
	public static class TrafficHistoryTableInfo{
		private String invstDt; 	//진행 조사 날짜
		private int totalCnt;		//총 조사 횟수
		private int completeCnt;	//조사 완료 횟수
		private int incompleteCnt;	//조사 미완료 횟수
		private String lcchgRsn;	//조사 거부 사유
		
	}
	
}
