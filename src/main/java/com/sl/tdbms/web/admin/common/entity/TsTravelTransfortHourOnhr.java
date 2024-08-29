package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 개인실태 나이대 년 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsTravelTransfortHourOnhr extends BaseEntity{
	@Id
	private String statsYymmdt;				//통계연
	private String exmnmngId;			//조사 관리 아이디
	private String departureTazCode;	//출발지 taz코드
	private String destinationTazCode;	//목적지 taz코드
	private long transfortHour;			//나이범위
}