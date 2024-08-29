package com.sl.tdbms.web.admin.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data //도로 관리
@EqualsAndHashCode(callSuper=true)
public class TcRoadMng extends BaseEntity{

    @Id
    private String roadCd; //도로 코드

    private String roadDescr; //도로 설명

    private String roadGrd; //도로 등급
    private BigDecimal roadLen;
    private Geometry geom; // gis
    private BigDecimal roadstartLat;
    private BigDecimal roadstartLon;
    private BigDecimal roadendLat;
    private BigDecimal roadendLon;

}
