package com.sl.tdbms.web.admin.common.dto.invst;

import java.util.List;
import java.util.stream.Collectors;

import com.sl.tdbms.web.admin.common.entity.TmSrvyAns;
import com.sl.tdbms.web.admin.common.entity.TmSrvyQstn;
import com.sl.tdbms.web.admin.common.entity.TmSrvySect;
import com.sl.tdbms.web.admin.common.enums.code.QstnTypeCd;

import lombok.Data;

@Data
public class TmSrvySectDetailDTO {
	
	private List<TmSrvySectInfo> tmSrvySectList;
	
	@Data
	public static class TmSrvySectInfo {
		
		private String sectId; //부문 아이디
		
		private String srvyId; //조사명
		
		private String sectTitle; //부문 제목
		
		private String sectSubtitle; //부문 보조 제목
		
		private String sectType; //부문 유형

		private String sectTypeNm; //부문 유형
		
		private Integer sectSqno; //부문 순번
		
		private List<TmSrvyQstnInfo> tmSrvyQstnList;
		
		@Data
		public static class TmSrvyQstnInfo {
			private String qstnId;
			private String sectId;
			private String qstnTitle;
			private QstnTypeCd qstnTypeCd;
			private Integer qstnSqno;
			private String srvyMetadataCd;
			private List<TmSrvyAnsInfo> tmSrvyAnsList;
			
			@Data
			public static class TmSrvyAnsInfo {
				private String ansId;
				private String qstnId;
				private String ansCnts;
				private String etcYn;
				private Integer ansSqno;
				
				public TmSrvyAnsInfo(TmSrvyAns tmSrvyAns) {
					this.ansId 			= tmSrvyAns.getAnsId();
					this.qstnId 		= tmSrvyAns.getQstnId();
					this.ansCnts 		= tmSrvyAns.getAnsCnts();
					this.etcYn 			= tmSrvyAns.getEtcYn();
					this.ansSqno 		= tmSrvyAns.getAnsSqno();
				}
				
			}
			
			public TmSrvyQstnInfo(TmSrvyQstn tmSrvyQstn) {
				this.sectId 		= tmSrvyQstn.getSectId();
				this.qstnId 		= tmSrvyQstn.getQstnId();
				this.qstnTitle 		= tmSrvyQstn.getQstnTitle();
				this.qstnTypeCd 	= tmSrvyQstn.getQstnType();
				this.qstnSqno 		= tmSrvyQstn.getQstnSqno();
				this.srvyMetadataCd	= tmSrvyQstn.getSrvyMetadataCd();
				
			}
			
			public void setTmSrvyAnsList(List<TmSrvyAns> tmSrvyAnsList) {
				this.tmSrvyAnsList = tmSrvyAnsList.stream()
		                .map(x -> new TmSrvyAnsInfo(x))
		                .collect(Collectors.toList());
			}
			
		}
		
		public TmSrvySectInfo(TmSrvySect tmSrvySect) {
			this.sectId 		= tmSrvySect.getSectId();
			this.srvyId 		= tmSrvySect.getSrvyId();
			this.sectTitle 		= tmSrvySect.getSectTitle();
			this.sectSubtitle 	= tmSrvySect.getSectSubtitle();
			this.sectType 		= tmSrvySect.getSectType();
			this.sectTypeNm 	= tmSrvySect.getSectTypeNm();
			this.sectSqno 		= tmSrvySect.getSectSqno();
		}
		
		public void setTmSrvyQstnList(List<TmSrvyQstn> tmSrvyQstnList) {
			this.tmSrvyQstnList = tmSrvyQstnList.stream()
	                .map(x -> new TmSrvyQstnInfo(x))
	                .collect(Collectors.toList());
		}
	}


	public void setTmSrvySectList(List<TmSrvySect> tmSrvySectList) {
		this.tmSrvySectList = tmSrvySectList.stream()
                .map(x -> new TmSrvySectInfo(x))
                .collect(Collectors.toList());
	}
	
}
