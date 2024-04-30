package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //게시판 파일그룹 정보
@EqualsAndHashCode(callSuper=true)
public class TlBbsFileGrp extends CreateEntity{

    @Id
    private String filegrpId; //파일 그룹 아이디

    private String bbsId; //게시판 아이디

}
