package com.sri.lanka.traffic.admin.common.dto.invst;

import java.util.List;

import com.sri.lanka.traffic.admin.common.entity.TmSrvyAns;
import com.sri.lanka.traffic.admin.common.entity.TmSrvyQstn;
import com.sri.lanka.traffic.admin.common.entity.TmSrvySect;
import com.sri.lanka.traffic.admin.common.enums.code.QstnTypeCd;
import com.sri.lanka.traffic.admin.common.enums.code.SectTypeCd;

import lombok.Data;

@Data
public class TmSrvySectUpdateDTO {
	
	private List<TmSrvySectUpdateInfo> tmSrvySectList;
	private String[] deleteAnsArray; 
	
	@Data
	public static class TmSrvySectUpdateInfo {
		
		private String exmnmngId;
		private String sectId;
		private String sectTitle;
		private String sectSubtitle;
		private SectTypeCd sectTypeCd; // ex) 가구실태,개인실태,교통 통해
		private Integer sectSqno;
		private List<TmSrvyQstnUpdateInfo> tmSrvyQstnList;
		
		@Data
		public static class TmSrvyQstnUpdateInfo {
			private String qstnId;
			private String sectId;
			private String qstnTitle;
			private QstnTypeCd qstnTypeCd;
			private Integer qstnSqno;
			private List<TmSrvyAnsUpdateInfo> tmSrvyAnsList;
			
			@Data
			public static class TmSrvyAnsUpdateInfo {
				private String ansId;
				private String qstnId;
				private String ansCnts;
				private Integer ansSqno;
				
				public TmSrvyAns toEntity(){
		            return TmSrvyAns.builder()
		            						.qstnId(qstnId)
		            						.ansCnts(ansCnts)
		            						.ansSqno(ansSqno)
			            					.build();
				}
				
			}
			
			public TmSrvyQstn toEntity(){
	            return TmSrvyQstn.builder()
	            					.qstnId(qstnId)
	            					.sectId(sectId)
	            					.qstnTitle(qstnTitle)
	            					.qstnTypeCd(qstnTypeCd)
	            					.qstnSqno(qstnSqno)
	            					.build();
			}
			
		}
		
		public TmSrvySect toEntity(){
            return TmSrvySect.builder()
            					.sectId(sectId)
            					.exmnmngId(exmnmngId)
            					.sectTitle(sectTitle)
            					.sectSubtitle(sectSubtitle)
            					.sectType(sectTypeCd)
            					.sectSqno(sectSqno)
            					.build();
		}
	}
	
}
