package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //조사그룹 마스터
@EqualsAndHashCode(callSuper=true)
public class TmExmnGrp extends CreateEntity{

    @Id
    private String exmngrpId; //조사 그룹 아이디

}
