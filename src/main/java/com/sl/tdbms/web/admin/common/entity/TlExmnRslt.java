package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.locationtech.jts.geom.Geometry;

import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;

import lombok.Data;

@Entity
@Data //조사결과 관리
public class TlExmnRslt{

    @Id
    private String exmnrsltId; //조사 결과 아이디

    private String exmnNm; //조사 명

    private String exmnPic; //조사 담당자

    private BigDecimal exmnLat; //조사 위도

    private BigDecimal exmnLon; //조사 경도

    private String exmnLc; //조사 위치

    private Geometry exmnGis; //조사 gis

    @Convert(converter = ExmnTypeCd.Converter.class)
    private ExmnTypeCd exmnType; //조사 유형
    
    private String exmnmngId; //조사 관리 아이디
    
    private LocalDateTime startDt; //시작 일시
    
    private LocalDateTime endDt; //종료 일시
    
    private String exmnDiv; //조사 구분

    @Convert(converter = ExmnSttsCd.Converter.class)
    private ExmnSttsCd sttsCd; //상태 코드

    private String exmnRange = "0"; //조사 범위
    
    private BigDecimal goalCnt = BigDecimal.ZERO; //조사 목표 개수
    
    private String dsdCd; //스리랑카사무국코드

    private String gnCd; //스리랑카지방사무국코드
    
    private String tazCd;
    
    private String cordonLine; //코든 라인
    
    private String tollBooth; //고속도로 나들목
    
    private String screenLine; //스크린 라인
    
    private String cstmYn = "N"; // 커스텀 여부
    
    private LocalDateTime registDt;
    
}
