package com.sri.lanka.traffic.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //개인통행조사 질문정보
public class TlIndvsPassQstn{

    @Id
    private String indvslqstnId; //개인 질문 아이디

    private String indvslpassId; //개인 통행 아이디

    private String qstnTitle; //질문 제목

    private BigDecimal qstnSqno; //질문 순번

}
