package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.enums.code.PollsterTypeCd;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //조사원 관리
@EqualsAndHashCode(callSuper=true)
public class TmExmnPollster extends CreateEntity{

    @Id
    private String pollsterId; //조사원 아이디

    private String exmnmngId; //조사 관리 아이디

    private String mngrId; //관리자 아이디

    private String pollsterNm; //조사원 명

    private String pollsterEmail; //조사원 이메일
    
    @Convert(converter = PollsterTypeCd.Converter.class)
    private PollsterTypeCd pollsterType; //조사원 유형(ex.팀원/팀장)

    private String pollsterTel; //조사원 전화번호

    private String pollsterBrdt; //조사원 생년월일

}
