package com.sl.tdbms.web.admin.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * 통과차량 1시간 통계
 */
@Data
@Entity
public class TsVhclOnhr{
    @Id
    private String instllcId;
    private String vhclDrct;
    private String vhclClsf;
    private long trfvlm;
    private long avgSpeed;
    private LocalDateTime statsDt;
}