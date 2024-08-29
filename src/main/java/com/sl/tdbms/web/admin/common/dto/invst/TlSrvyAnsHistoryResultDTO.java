package com.sl.tdbms.web.admin.common.dto.invst;

import java.util.List;

import lombok.Data;

@Data
public class TlSrvyAnsHistoryResultDTO {
	
	private List<SurveyResultTableInfo> surveyResultTableList;
//	private PagingUtils paging;
	
	@Data
	public static class SurveyResultTableInfo{
		private String sectType;
		private String qstnSqno;
		private String qstnNm;
		private String ansCnt;
		private String sectSqno;
		private String cdnmEng;
		private String srvyrsltId;
	}
}
