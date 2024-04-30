package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //공통코드 그룹관리
@EqualsAndHashCode(callSuper=true)
public class TcCdGrp extends CreateEntity{

    @Id
    private String grpcdId = CommonUtils.getUuid(); //그룹 코드 아이디

    private String grpCd; //그룹 코드

    private String grpcdNm; //그룹 코드 명

    private String grpcdDescr; //그룹 코드 설명
    
    private String useYn; //사용 여부

}
