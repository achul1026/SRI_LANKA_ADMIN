package com.sri.lanka.traffic.admin.common.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //개인통행조사 결과정보
@EqualsAndHashCode(callSuper=true)
public class TlIndvsPassRslt extends CreateEntity{

    @Id
    private String indvslpassId; //개인 통행 아이디

    private LocalDateTime dptreDt; //출발 일시

    private String dptreLc; //출발 위치

    private BigDecimal exmnLat; //조사 위도

    private BigDecimal exmnLon; //조사 경도

    private String exmnGis; //조사 gis

    private String dptrepointType; //출발 지점 유형

    private String dptrepointEtc; //출발 지점 기타

}
