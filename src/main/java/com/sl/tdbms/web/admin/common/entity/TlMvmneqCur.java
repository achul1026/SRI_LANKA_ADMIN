package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;

@Entity
@Data //이동형장비 설치위치 관리
public class TlMvmneqCur {
	@Id
	private String instllcId; //설치 위치 아이디
	
	private String instllcNm; //설치위치 명
	
//	private String instllcDescr; //설치위치 설명
	
//	private String eqpmntId; //장비 아이디
	
	private BigDecimal lat; //위도
	
	private BigDecimal lon; //경도
	
//	private LocalDateTime clctDt; //등록 일시
	
	public TlMvmneqCur(){};
	
	@Builder
	public TlMvmneqCur(String instllcId, String instllcNm
//			, String instllcDescr, String eqpmntId, LocalDateTime clctDt
			, BigDecimal lat
			, BigDecimal lon
			){
		this.instllcId = instllcId;
		this.instllcNm = instllcNm;
//		this.instllcDescr = instllcDescr;
//		this.eqpmntId = eqpmntId;
		this.lat = lat;
		this.lon = lon;
//		this.clctDt = clctDt;
	}
	
}
