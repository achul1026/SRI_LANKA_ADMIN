package com.sri.lanka.traffic.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //설문조사 관리
@EqualsAndHashCode(callSuper=true)
public class TmSrvyMng extends CreateEntity{

    @Id
    private String srvyId; //설문 아이디

    private String exmnmngId; //조사 관리 아이디

    private String srvyType; //설문 유형

    private BigDecimal srvygoalCnt; //설문 목표 수
    
}
