package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.sri.lanka.traffic.admin.common.enums.code.QstnTypeCd;

import lombok.Builder;
import lombok.Data;

@Entity
@Data //설문질문 관리
public class TmSrvyQstn{

    @Id
    private String qstnId; //질문 아이디

    private String sectId; //부문 아이디

    private String qstnTitle; //질문 제목
    
    @Convert(converter = QstnTypeCd.Converter.class)
    private QstnTypeCd qstnType; //질문 유형

    private Integer qstnSqno; //질문 순번
    
    public TmSrvyQstn(){}
    
    @Builder
	public TmSrvyQstn(String qstnId, String sectId, String qstnTitle, QstnTypeCd qstnTypeCd, Integer qstnSqno) {
		this.qstnId 		= qstnId;
		this.sectId 		= sectId;
		this.qstnTitle 		= qstnTitle;
		this.qstnType 		= qstnTypeCd;
		this.qstnSqno 		= qstnSqno;
		
	}
}
