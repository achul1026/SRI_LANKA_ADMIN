package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 개인통행 요금정보 일 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsTravelTransfortInfoYy extends BaseEntity{
    @Id
    private String statsYy;
    private String exmnmngId;
    private String departureTazCode;
    private String destinationTazCode;
    private long transfortCount;
    private long cnt;
    private long avgTransfortMinutes;
}
