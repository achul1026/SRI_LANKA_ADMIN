package com.sl.tdbms.web.admin.common.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@Data //설문조사 결과정보
@EntityListeners(AuditingEntityListener.class)
public class TlSrvyRslt {

    @Id
    private String srvyrsltId; //설문 결과 아이디

    private String exmnrsltId; //조사 결과 아이디
    
    private String startlcNm; //시작위치 명
    
    private String endlcNm; //종료위치 명
    
    private LocalDate exmnstartDt; //조사 기간 시작 일시
    
    private LocalDate exmnendDt; //조사 기간 종료 일시
    
    private String pollsterLc; //조사원 위치
    
    private String pollsterNm; //조사원 명
    
    private String pollsterTel; //조사원 전화번호
    
    private String pollsterEmail; //조사원 이메일
    
    private String gnCd; //스리랑카지방사무국코드
    
    private String cordonLine; //코든 라인
    
    private String tollBooth; //고속도로 나들목
    
    private String screenLine; //스크린 라인
    
    @CreatedDate
	@Column(updatable = false ,nullable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime registDt; //등록일

}
