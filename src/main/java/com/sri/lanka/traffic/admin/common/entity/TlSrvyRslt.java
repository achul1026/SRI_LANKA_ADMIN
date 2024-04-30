package com.sri.lanka.traffic.admin.common.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //설문조사 결과정보
public class TlSrvyRslt {

    @Id
    private String srvyrsltId; //설문 결과 아이디

    private String exmnrsltId; //조사 결과 아이디
    
    private LocalDate exmnstartDt; //조사 기간 시작 일시
    
    private LocalDate exmnendDt; //조사 기간 종료 일시
    
    private String pollsterLc; //조사원 위치

}
