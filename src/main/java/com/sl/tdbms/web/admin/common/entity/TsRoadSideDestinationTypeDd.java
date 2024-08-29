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
public class TsRoadSideDestinationTypeDd extends BaseEntity{
    @Id
    private String statsYymmdd;
    private String exmnmngId;
    private String departureTazCode;
    private String destinationTazCode;
    private String destinationType;
    private long cnt;
}
