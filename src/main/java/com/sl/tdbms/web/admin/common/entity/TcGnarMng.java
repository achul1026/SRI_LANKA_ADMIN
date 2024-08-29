package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //지방사무국 구역관계 관리
@EqualsAndHashCode(callSuper=true)
public class TcGnarMng extends BaseEntity{

    @Id
    private int clsfNo; //분류번호
    
    private String dstrctCd; //구역코드
    
    private String gnId; //스리랑카지방사무국아이디
    
    private BigDecimal distrbCnt; //분포수

}
