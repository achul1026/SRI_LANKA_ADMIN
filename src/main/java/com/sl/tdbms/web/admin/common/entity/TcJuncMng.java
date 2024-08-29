package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //교차지점 관리
@EqualsAndHashCode(callSuper=true)
public class TcJuncMng extends BaseEntity{

    @Id
    private String juncId; //교차지점 아이디

    private String roadCd; //도로 코드

    private BigDecimal lat; //위도

    private BigDecimal lon; //경도

    private String juncDiv; //교차지점 구분

    private String juncDescr; //교차지점 설명

}
