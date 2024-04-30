package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //개인통행조사 답변정보
public class TlIndvsPassAns{

    @Id
    private String indvslansId; //개인 답변 아이디

    private String indvslqstnId; //개인 질문 아이디

    private String ansCnts; //답변 내용

}
