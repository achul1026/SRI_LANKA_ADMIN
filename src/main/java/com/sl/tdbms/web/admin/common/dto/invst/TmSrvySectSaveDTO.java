package com.sl.tdbms.web.admin.common.dto.invst;

import java.time.LocalDateTime;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmSrvyAns;
import com.sl.tdbms.web.admin.common.entity.TmSrvyInfo;
import com.sl.tdbms.web.admin.common.entity.TmSrvyQstn;
import com.sl.tdbms.web.admin.common.entity.TmSrvySect;
import com.sl.tdbms.web.admin.common.enums.code.QstnTypeCd;

import lombok.Data;

@Data
public class TmSrvySectSaveDTO {
	
	private List<TmSrvySectSaveInfo> tmSrvySectList;
	private String srvyId;
	private String srvyTitle;
	private String srvyType;
	private String cstmYn;
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
			private String metadataCd;
			private Integer qstnSqno;
			private List<TmSrvyAnsSaveInfo> tmSrvyAnsList;
			
			@Data
			public static class TmSrvyAnsSaveInfo {
				private String qstnId;
				private String ansCnts;
				private String etcYn = "N";
				private Integer ansSqno;
				
				public TmSrvyAns toEntity(){
		            return TmSrvyAns.builder()
		            						.qstnId(qstnId)
		            						.ansCnts(ansCnts)
		            						.ansSqno(ansSqno)
		            						.etcYn(etcYn)
			            					.build();
				}
				
			}
			
			public TmSrvyQstn toEntity(){
	            return TmSrvyQstn.builder()
	            					.qstnId(qstnId)
	            					.sectId(sectId)
	            					.qstnTitle(qstnTitle)
	            					.qstnTypeCd(qstnTypeCd)
									.srvyMetadataCd(metadataCd)
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
        					.cstmYn(cstmYn)
        					.startDt(startDt)
        					.endDt(endDt)
        					.build();
	}
	
	
}
