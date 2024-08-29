package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //파일 정보
@EqualsAndHashCode(callSuper=true)
public class TlBbsFile extends CreateEntity{

    @Id
    private String fileId = CommonUtils.getUuid(); //파일 아이디

    private String filegrpId; //파일 그룹 아이디

    private String fileNm; //파일 명

    private String orgFilenm; //원본 파일 명

    private String fileFilext; //파일 확장자

    private String filePath; //파일 경로

    private BigDecimal fileSize; //파일 크기

}
