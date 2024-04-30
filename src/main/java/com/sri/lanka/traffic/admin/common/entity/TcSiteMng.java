package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //사이트 관리
@EqualsAndHashCode(callSuper=true)
public class TcSiteMng extends CreateEntity{

    @Id
    private String siteId = CommonUtils.getUuid(); //사이트 아이디

    private String siteNm; //사이트 명

    private String siteclsfCd; //사이트 분류 코드

    private String siteUrl; //사이트 url

    private String dspyYn; //표출 여부
}
