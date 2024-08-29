package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.locationtech.jts.geom.Geometry;
import org.springframework.format.annotation.DateTimeFormat;

import com.sl.tdbms.web.admin.common.enums.code.ColorTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //조사관리
@EqualsAndHashCode(callSuper=true)
public class TmExmnMng extends CreateEntity{

    @Id
    private String exmnmngId = CommonUtils.getUuid(); //조사 관리 아이디

    private String exmngrpId; //조사 그룹 아이디
    
    private String srvyId; //설문 정보 아이디

    private String usermngId; //관리자 아이디

    private String exmnNm; //조사 명
    
    @Convert(converter = ExmnTypeCd.Converter.class)
    private ExmnTypeCd exmnType; //조사 유형

    private String exmnpicId; //조사 담당자 아이디

    private String exmnLc; //조사 위치

    private String exmnRange = "0"; //조사 범위

    private BigDecimal lat; //위도

    private BigDecimal lon; //경도

    private String roadCd; //도로 명

    private String roadDescr; //경로
    
    private BigDecimal laneCnt; //차로 수

    private String exmnDiv; //조사 구분

    private BigDecimal exmnNop; //조사 인원수

    @Convert(converter = ColorTypeCd.Converter.class)
    private ColorTypeCd colrCd; //색상 코드
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDt; //시작 일시
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDt; //종료 일시

    @Convert(converter = ExmnSttsCd.Converter.class)
    private ExmnSttsCd sttsCd; //상태 코드

    private Geometry exmnGis; //조사 gis

    private BigDecimal goalCnt = BigDecimal.ZERO; //조사 목표 개수
    
    private String partcptCd; //참가 코드
    
    private String dsdCd; //스리랑카사무국코드

    private String gnCd; //스리랑카지방사무국코드
    
    private String tazCd;
    
    private String cordonLine; //코든 라인
    
    private String tollBooth; //고속도로 나들목
    
    private String screenLine; //스크린 라인
    
    private String cstmYn = "N"; //커스텀 여부
    
    private String exmnDistance; //조사 위치
    
	public void setExmnmngId(String exmnmngId) {
		this.exmnmngId = CommonUtils.isNull(exmnmngId) ? CommonUtils.getUuid() : exmnmngId;
	}
    
}
