package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Builder;
import lombok.Data;

@Entity
@Data //설문부문 관리
public class TmSrvySect{

    @Id
    private String sectId; //부문 아이디

    private String srvyId; //조사 아이디

    private String sectTitle; //부문 제목

    private String sectSubtitle; //부문 보조 제목
    
    private String sectType; //부문 유형
    
    private Integer sectSqno; //부문 순번

    
    @Transient
    private String sectTypeNm; //부문 유형
    
    public TmSrvySect(){}
    
    @Builder
	public TmSrvySect(String sectId, String srvyId, String sectTitle, String sectSubtitle, String sectType, Integer sectSqno) {
		this.sectId 		= sectId;
		this.srvyId 		= srvyId;
		this.sectTitle 		= sectTitle;
		this.sectSubtitle 	= sectSubtitle;
		this.sectType 		= sectType;
		this.sectSqno 		= sectSqno;
	}
}
