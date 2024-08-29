package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 노측면접 출발지유형 일 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsRoadSideDepartureTypeOnhr extends BaseEntity{
    @Id
    private String statsYymmdt;			//통계년월일시
    private String exmnmngId;			//조사 관리 아이디
    private String departureTazCode;	//출발지 taz 코드
    private String destinationTazCode;	//목적지 taz 코드
    private String departureType;		//출발지 유형
    private long cnt;
}
