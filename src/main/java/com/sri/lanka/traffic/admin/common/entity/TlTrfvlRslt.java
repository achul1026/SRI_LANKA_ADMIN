package com.sri.lanka.traffic.admin.common.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //교통량조사 결과정보
public class TlTrfvlRslt {

	@Id
    private String trfvlmexmnId; //교통량 조사 아이디

    private String exmnrsltId; //조사 결과 아이디
    
    private String startlcNm; //시작위치 명
    
    private String endlcNm; //종료위치 명
    
    private BigDecimal pollsterLat; //조사원 위도
    
    private BigDecimal pollsterLon; //조사원 경도
    
    private String pollsterLc; //조사 결과 아이디조사원 위치
    
    private LocalDateTime exmnstartDt; //조사 시작 일시

    private LocalDateTime exmnendDt; //조사 종료 일시
    
    private String pollsterRange; //조사원 범위
    
    private String pollsterGis; //조사원 GIS
    
    private String lcchgRsn; //위치 변경 사유

}
