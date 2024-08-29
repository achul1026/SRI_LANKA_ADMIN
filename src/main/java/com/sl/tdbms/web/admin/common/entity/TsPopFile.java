package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
public class TsPopFile extends CreateEntity {

    @Id
    @Column(name = "fileId", updatable = false, nullable = false)
    private String fileId; // 파일 아이디

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popmngId")
    private TsPopMng tsPopMng; // 인구통계데이터 아이디
    
    private String orgFileNm; //원본 파일명

    private String fileNm; //파일명

    private String fileFilext; //파일 확장자

    private String filePath; //파일 경로

    private BigDecimal fileSize; //파일 크기
}
