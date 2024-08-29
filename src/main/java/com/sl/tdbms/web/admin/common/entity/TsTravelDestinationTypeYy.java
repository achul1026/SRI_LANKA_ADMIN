package com.sl.tdbms.web.admin.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 개인통행 목적별 도착시간 일 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsTravelDestinationTypeYy extends BaseEntity{
    @Id
    private String statsYy;
    private String exmnmngId;
    private String departureTazCode;
    private String destinationTazCode;
    private String destinationType;
    private long cnt;
}
