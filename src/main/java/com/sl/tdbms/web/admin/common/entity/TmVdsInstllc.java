package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //VDS 설치위치 관리
@EqualsAndHashCode(callSuper=true)
public class TmVdsInstllc extends BaseEntity {

	@Id
	private String instllcId; //설치 위치 아이디
	
	private String instllcNm; //설치위치 명
	
	private String instllcDescr; //설치위치 설명
	
	private String cameraId; //카메라 아이디
	
	private BigDecimal lat; //위도
	
	private BigDecimal lon; //경도
	
	public TmVdsInstllc(){};
	
	@Builder
	public TmVdsInstllc(String instllcId, String instllcNm
			, String instllcDescr, String cameraId
			, BigDecimal lat, BigDecimal lon){
		this.instllcId = instllcId;
		this.instllcNm = instllcNm;
		this.instllcDescr = instllcDescr;
		this.cameraId = cameraId;
		this.lat = lat;
		this.lon = lon;
		this.lon = lon;
	}
	
}
