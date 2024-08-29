package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //교통량 조사
@EqualsAndHashCode(callSuper=true)
public class TmTrfvlmExmn extends CreateEntity{

    @Id
    private String trfvlmexmnId; //교통량 조사 아이디

    private String exmnmngId; //조사 관리 아이디

    private String exmnType; //조사 유형

}
