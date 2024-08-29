package com.sl.tdbms.web.admin.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 연령 별 인구 수 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsRoadSidePassageTypeOnhr extends BaseEntity{
    @Id
    private String statsYymmdt;
    private String exmnmngId;
    private String departureTazCode;
    private String destinationTazCode;
    private String passageType;
    private String avgPassengers;
    private long cnt;
}
