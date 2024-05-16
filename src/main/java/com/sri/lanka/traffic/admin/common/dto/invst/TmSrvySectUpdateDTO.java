package com.sri.lanka.traffic.admin.common.dto.invst;

import java.time.LocalDateTime;
import java.util.List;

import com.sri.lanka.traffic.admin.common.entity.TmSrvyAns;
import com.sri.lanka.traffic.admin.common.entity.TmSrvyQstn;
import com.sri.lanka.traffic.admin.common.entity.TmSrvySect;
import com.sri.lanka.traffic.admin.common.enums.code.QstnTypeCd;

import lombok.Data;

@Data
public class TmSrvySectUpdateDTO {
	
	private List<TmSrvySectUpdateInfo> tmSrvySectList;
	private String[] deleteSectArray; 
	private String[] deleteQstnArray; 
	private String[] deleteAnsArray; 
	private String srvyId;
	private String srvyTitle;
	private String srvyType;
	private LocalDateTime startDt;
	private LocalDateTime endDt;
	
	@Data
	public static class TmSrvySectUpdateInfo {
		
		private String srvyId;
		private String sectId;
		private String sectTitle;
		private String sectSubtitle;
		private String sectTypeCd; // ex) 가구실태,개인실태,교통 통해
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
            					.srvyId(srvyId)
            					.sectTitle(sectTitle)
            					.sectSubtitle(sectSubtitle)
            					.sectType(sectTypeCd)
            					.sectSqno(sectSqno)
            					.build();
		}
	}
	
}
