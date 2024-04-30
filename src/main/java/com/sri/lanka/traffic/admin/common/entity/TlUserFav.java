package com.sri.lanka.traffic.admin.common.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //조사위치 즐겨찾기 관리
public class TlUserFav{

    @Id
    private String favId; //즐겨찾기 아이디

    private String usermngId; //사용자 관리 아이디

    private String placeCd; //장소 코드

    private BigDecimal lat; //위도

    private BigDecimal lon; //경도

    private String exmnLc; //조사 위치

    private String exmnGis; //조사 gis

    private LocalDateTime registDt; //등록 일시

}
