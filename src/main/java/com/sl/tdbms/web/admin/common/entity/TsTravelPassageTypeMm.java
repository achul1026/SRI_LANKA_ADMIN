package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 개인통행 통행 수단별 월 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsTravelPassageTypeMm extends BaseEntity{
    @Id
    private String statsYymm;
    private String exmnmngId;
    private String departureTazCode;
    private String destinationTazCode;
    private String passageType;
    private long cnt;
    private long avgPassengers;
    private long avgTravelTime;
}
