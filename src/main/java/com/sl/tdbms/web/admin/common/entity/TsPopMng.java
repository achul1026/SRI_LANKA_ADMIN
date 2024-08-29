package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.sl.tdbms.web.admin.common.enums.code.PopStatsTypeCd;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //인구 통계 데이터 마스터테이블
@EqualsAndHashCode(callSuper=true)
public class TsPopMng extends CreateEntity{

    @Id
    private String popmngId; // 인구통계데이터 아이디

    @OneToOne(mappedBy = "tsPopMng", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TsPopFile tsPopFile; //파일아이디
    
    private String fileId;

    private String popmngTitle; //인구통계데이터 제목

    private String popmngCnts; //인구통계데이터 설명

    @Convert(converter = PopStatsTypeCd.Converter.class)
    private PopStatsTypeCd popmngType; //인구통계데이터 유형

    private String userBffltd; //관리기관
    
    private String startYear;	//시작일
    
    private String endYear;	//종료일
    
    public void setTsPopFile(TsPopFile tsPopFile) {
    	this.tsPopFile = tsPopFile;
    	if(tsPopFile != null) {
    		this.fileId = tsPopFile.getFileId();
    	}
        tsPopFile.setTsPopMng(this);
    }

}
