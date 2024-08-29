package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //게시판 정보
@EqualsAndHashCode(callSuper=true)
public class TlBbsInfo extends BaseEntity{

    @Id
    private String bbsId = CommonUtils.getUuid(); //게시판 아이디

    private String bbsType; //게시판 유형

    private String bbsTitle; //게시판 제목

    private String bbsCnts; //게시판 내용

    private String dspyYn; //표출 여부
    
    private int viewCnt = 0; //조회수

}
