package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //OPENAPI 제공 항목 관리
public class TmApiPvsnitem {

    private String srvcId; //서비스 아이디
    
    @Id
    private String itemId; //항목 아이디
    
    private BigDecimal itemSqno; //항목 순번
    
    private String itemNm; //항목 명
    
    private String itemType; //항목 유형
    
    private BigDecimal itemLen; //항목 길이
    
    private String esntlYn; //필수 여부
    
    private String itemDescr; //항목 설명
    
    private String smplData; //셈플 데이터
    
}
