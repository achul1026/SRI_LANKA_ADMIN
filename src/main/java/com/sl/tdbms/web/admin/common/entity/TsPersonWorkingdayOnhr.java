package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 개인실태 평균 근무일수 1시간 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsPersonWorkingdayOnhr extends BaseEntity{
	@Id
	private String statsYymmdt;			//통계연월일시
	private String exmnmngId;			//조사 관리 아이디
	private String departureTazCode;	//출발지 taz코드
	private String destinationTazCode;	//목적지 taz코드
	private String avgWorkingday;		//평균 근무 일수
	private long cnt;				//집계 수
}
