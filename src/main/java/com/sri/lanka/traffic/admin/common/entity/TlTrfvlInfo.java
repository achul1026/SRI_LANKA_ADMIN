package com.sri.lanka.traffic.admin.common.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //교통량 결과 정보
public class TlTrfvlInfo {

    @Id
    private String trfvlmrsltId; //교통량 결과 정보 아이디

    private String trfvlmexmnId; //교통량 조사 아이디
    
    private LocalDate ftnminunitTime; //15분단위 시간
    
    private LocalDate mvmnmeanType; //이동 수단 유형
    
    private BigDecimal trfvlm; //교통량 
    
}
