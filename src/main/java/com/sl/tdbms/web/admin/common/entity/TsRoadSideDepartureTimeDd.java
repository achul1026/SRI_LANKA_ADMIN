package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 노측면접 출발시간별 월 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsRoadSideDepartureTimeDd extends BaseEntity{
    @Id
    private String statsYymmdd;			//통계년월일
    private String exmnmngId;			//조사 관리 아이디
    private String departureTazCode;	//출발지 taz 코드
    private String destinationTazCode;	//목적지 taz 코드
    private String departureTime;		//출발 시간
    private String tripPurpose;			//이동 목적
    private long cnt;
}