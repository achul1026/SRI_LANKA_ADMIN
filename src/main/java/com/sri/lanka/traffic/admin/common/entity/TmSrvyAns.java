package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Builder;
import lombok.Data;

@Entity
@Data //설문답변 정보
public class TmSrvyAns{

    @Id
    private String ansId = CommonUtils.getUuid(); //답변 아이디

    private String qstnId; //질문 아이디

    private String ansCnts; //답변 내용

    private Integer ansSqno; //답변 순번
    
    public TmSrvyAns(){}
    
    @Builder
	public TmSrvyAns(String qstnId, String ansCnts, Integer ansSqno) {
		this.qstnId 		= qstnId;
		this.ansCnts 		= ansCnts;
		this.ansSqno 		= ansSqno;
		
	}
    
}
