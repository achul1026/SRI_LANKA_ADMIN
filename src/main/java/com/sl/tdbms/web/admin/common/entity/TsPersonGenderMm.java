package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 개인실태 성별 월 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsPersonGenderMm extends BaseEntity{
	@Id
	private String statsYymm;			//통계연월일
	private String exmnmngId;			//조사 관리 아이디
	private String departureTazCode;	//출발지 taz코드
	private String destinationTazCode;	//목적지 taz코드
	private String gender;				//성별
	private long cnt;				//집계 수
}
