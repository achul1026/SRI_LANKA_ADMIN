package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;

@Entity
@Data // 고정형장비 설치위치 관리 
public class TlFixedCur {
	
	@Id
	private String instllcId; //설치 위치 아이디
	
	private String instllcNm; //설치위치 명
	
	private BigDecimal lat; //위도
	
	private BigDecimal lon; //경도
	
	public TlFixedCur() {};
	
	@Builder
	public TlFixedCur(String instllcId, String instllcNm, BigDecimal lat, BigDecimal lon) {
		this.instllcId = instllcId;
		this.instllcNm = instllcNm;
		this.lat = lat;
		this.lon = lon;
	}
}
