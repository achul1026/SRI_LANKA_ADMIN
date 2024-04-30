package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //권한 그룹 관리
@EqualsAndHashCode(callSuper=true)
public class TcAuthGrp extends BaseEntity {

    @Id
    private String authgrpId = CommonUtils.getUuid(); //권한 그룹 아이디

    private String authgrpNm; //권한 그룹 명
    
    private String authgrpDescr; //권한 그룹 설명
    
    private String bffltdCd; //소속 코드

    private String useYn; //사용 여부

}   
