package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

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
    
    private String etcYn = "N"; //기타 유무
    
    public TmSrvyAns(){}
    
    @Builder
	public TmSrvyAns(String qstnId, String ansCnts, Integer ansSqno, String etcYn) {
		this.qstnId 		= qstnId;
		this.ansCnts 		= ansCnts;
		this.ansSqno 		= ansSqno;
		this.etcYn 			= etcYn;
	}
}