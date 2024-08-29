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
public class TsTravelFeeInfoMm extends BaseEntity{
    @Id
    private String statsYymm;
    private String exmnmngId;
    private String departureTazCode;
    private String destinationTazCode;
    private String feeType;
    private long cnt;
    private long avgToll;
}
