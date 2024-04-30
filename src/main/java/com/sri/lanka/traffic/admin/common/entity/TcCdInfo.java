package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //공통코드 관리
@EqualsAndHashCode(callSuper=true)
public class TcCdInfo extends CreateEntity{

    @Id
    private String cdId = CommonUtils.getUuid(); //코드 아이디

    private String grpcdId; //그룹 코드 아이디

    private String cd; //코드

    private String cdNm; //코드 명

    private String cdDescr; //코드 설명

    private String useYn; //사용 여부

}
