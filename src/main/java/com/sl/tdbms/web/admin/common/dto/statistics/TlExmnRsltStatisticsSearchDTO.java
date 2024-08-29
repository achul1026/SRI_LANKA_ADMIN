package com.sl.tdbms.web.admin.common.dto.statistics;

import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;

@Data
public class TlExmnRsltStatisticsSearchDTO {

	private String searchDate; //검색 날짜
	private String roadNm; // 도로명
	private String roadLocation; // 도로에서의 위치
	private String dsdCd; // dsdCd
	private String gnCd; // gnCd
	private String searchCd; // searchCd
	private String startlcNm; //조사방향 출발 위치
	private String endlcNm; //조사방향 종료 위치
	private String exmnLc; //조사위치
	private ExmnTypeCd exmnTypeCd; //조사 타입
	private String destinationDsdCd; // 종점 dsdCd
	private String destinationGnCd; // 종점 gnCd
	private String destinationSearchCd; // 종점 검색
	private String destinationExmnLc; //종점 조사위치
	private String searchSrvyId; //설문 시트 ID
	private String searchContent;	//검색 컨텐츠
	
	private int originSubstringIdx = 4;
	private int destinationSubstringIdx = 4;
	private String searchRoadCd;
	private String searchExmnDistance;

	public void setDsdCd(String dsdCd) {
		this.dsdCd = dsdCd;
		this.searchCd = dsdCd;
	}
	public void setGnCd(String gnCd) {
		if(!CommonUtils.isNull(gnCd)) {
			this.originSubstringIdx = 7;
			this.searchCd = gnCd;
		}
		this.gnCd = gnCd;
	}
	public void setDestinationDsdCd(String destinationDsdCd) {
		this.destinationDsdCd = destinationDsdCd;
		this.destinationSearchCd = destinationDsdCd;
	}
	public void setDestinationGnCd(String destinationGnCd) {
		if(!CommonUtils.isNull(destinationGnCd)) {
			this.destinationSubstringIdx = 7;
			this.destinationSearchCd = destinationGnCd;
		}
		this.destinationGnCd = destinationGnCd;
	}
	public void setStartlcNm(String startlcNm) {
		this.startlcNm = CommonUtils.isNull(startlcNm) ? null : startlcNm;
	}

	public void setEndlcNm(String endlcNm) {
		this.endlcNm = CommonUtils.isNull(endlcNm) ? null : endlcNm;
	}
}
