package com.sri.lanka.traffic.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //설문조사 답변정보
public class TlSrvyAns {

    @Id
    private String srvyansId; //설문 답변 아이디

    private String srvyrsltId; //설문 결과 아이디
    
    private String sectType; //섹션 타입
    
    private String qstnTitle; //질문 제목
    
    private BigDecimal qstnSqno; //질문 순서
    
    private String ansCnts; //답변 내용

}
