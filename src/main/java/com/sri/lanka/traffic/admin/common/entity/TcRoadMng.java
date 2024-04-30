package com.sri.lanka.traffic.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //도로 관리
@EqualsAndHashCode(callSuper=true)
public class TcRoadMng extends BaseEntity{

    @Id
    private String roadCd; //도로 코드

    private String roadDescr; //도로 설명

    private String roadGrd; //도로 등급

    private BigDecimal roadLen; //도로 길이

}
