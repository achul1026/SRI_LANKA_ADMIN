package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.enums.code.QstnTypeCd;

import lombok.Data;

@Entity
@Data //설문조사 답변정보
public class TlSrvyAns {

	@Id
    private String srvyansId; //설문 답변 아이디

    private String srvyrsltId; //설문 결과 아이디
    
    private String sectType; //섹션 타입
    
    private BigDecimal sectSqno;

    private String qstnTitle; //질문 제목

    private QstnTypeCd qstnType; //질문 타입
    
    private BigDecimal qstnSqno; //질문 순서

    private String ansCnts; //답변 내용

    private String etcYn; // 기타 유무무

    private String srvyMetadataCd;

}
