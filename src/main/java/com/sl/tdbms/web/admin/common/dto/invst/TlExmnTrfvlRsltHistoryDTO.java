package com.sl.tdbms.web.admin.common.dto.invst;

import java.util.List;

import lombok.Data;

@Data
public class TlExmnTrfvlRsltHistoryDTO {

	private List<LcchgRsnInfo> lcchgRsnList;
	private List<TrafficHistoryTableInfo> trafficHistoryTableList;

	@Data
	public static class LcchgRsnInfo{
		private String lcchgRsn; //위치 변경 사유
		private String lcchgRsnDt; //위치 변경 사유 날짜
	}
	@Data
	public static class TrafficHistoryTableInfo{
		private String surveyDateTime;
		private String dataYn;
		private int mclcnt;
		private int twlcnt;
		private int carcnt;
		private int vancnt;
		private int mbucnt;
		private int lbucnt;
		private int lgvcnt;
		private int mg1cnt;
		private int mg2cnt;
		private int hg3cnt;
		private int ag3cnt;
		private int ag4cnt;
		private int ag5cnt;
		private int ag6cnt;
		private int fvhcnt;
	}
}
