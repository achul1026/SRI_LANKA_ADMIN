package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //공통코드 그룹관리
@EqualsAndHashCode(callSuper=true)
public class TcShapeSrlk extends CreateEntity{

    @Id
    private String dstrctId;
    private String dstrctGis;
    private String dstrctCd;
    private BigDecimal cntrLat;
    private BigDecimal cntrLon;

}
