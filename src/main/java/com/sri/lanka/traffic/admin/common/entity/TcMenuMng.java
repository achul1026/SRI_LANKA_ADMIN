package com.sri.lanka.traffic.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.sri.lanka.traffic.admin.common.converter.EmptyStringToNullConverter;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //메뉴 관리
@EqualsAndHashCode(callSuper=true)
public class TcMenuMng extends BaseEntity{

    @Id
    private String menuId = CommonUtils.getUuid(); //메뉴 아이디

    private String menuCd; //메뉴 코드

    @Convert(converter = EmptyStringToNullConverter.class)
    private String uppermenuCd; //상위 메뉴 코드

    private String menuNm; //메뉴 명

    private String menuDescr; //메뉴 설명

    private BigDecimal menuSqno; //메뉴 순번

    private String uppermenuUrlpttrn; //상위 메뉴 url 패턴

    private String menuUrlpttrn; //메뉴 url 패턴

    private String bscmenuYn; //기본 메뉴 여부

    private String useYn; //사용 여부

}
