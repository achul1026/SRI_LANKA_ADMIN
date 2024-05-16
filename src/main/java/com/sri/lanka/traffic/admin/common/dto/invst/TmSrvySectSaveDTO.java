package com.sri.lanka.traffic.admin.common.dto.invst;

import java.time.LocalDateTime;
import java.util.List;

import com.sri.lanka.traffic.admin.common.entity.TmSrvyAns;
import com.sri.lanka.traffic.admin.common.entity.TmSrvyInfo;
import com.sri.lanka.traffic.admin.common.entity.TmSrvyQstn;
import com.sri.lanka.traffic.admin.common.entity.TmSrvySect;
import com.sri.lanka.traffic.admin.common.enums.code.QstnTypeCd;

import lombok.Data;

@Data
public class TmSrvySectSaveDTO {
	
	private List<TmSrvySectSaveInfo> tmSrvySectList;
	private String srvyId;
	private String srvyTitle;
	private String srvyType;
	private LocalDateTime startDt;
	private LocalDateTime endDt;
	
	@Data
	public static class TmSrvySectSaveInfo {
		
		private String srvyId;
		private String sectId;
		private String sectTitle;
		private String sectSubtitle;
		private String sectTypeCd; // ex) 가구실태,개인실태,교통 통해
		private Integer sectSqno;
		private List<TmSrvyQstnSaveInfo> tmSrvyQstnList;
		
		@Data
		public static class TmSrvyQstnSaveInfo {
			private String qstnId;
			private String sectId;
			private String qstnTitle;
			private QstnTypeCd qstnTypeCd;
			private Integer qstnSqno;
			private List<TmSrvyAnsSaveInfo> tmSrvyAnsList;
			
			@Data
			public static class TmSrvyAnsSaveInfo {
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
	
	public TmSrvyInfo toEntity(){
        return TmSrvyInfo.builder()
        					.srvyId(srvyId)
        					.srvyTitle(srvyTitle)
        					.srvyType(srvyType)
        					.startDt(startDt)
        					.endDt(endDt)
        					.build();
	}
	
	
}
