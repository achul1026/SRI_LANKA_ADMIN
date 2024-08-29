package com.sl.tdbms.web.admin.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 개인통행 요금정보 일 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsTravelUseHighwayOnhr extends BaseEntity{
    @Id
    private String statsYymmdt;
    private String exmnmngId;
    private String departureTazCode;
    private String destinationTazCode;
    private String useHighway;
    private long cnt;
}
